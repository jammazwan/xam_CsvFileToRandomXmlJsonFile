package jammazwan.xam;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;

public class XamRoutes extends RouteBuilder {

	@Autowired
	private XamBean xamBean;

	@Override
	public void configure() throws Exception {
		from("file:../jammazwan.shared/src/main/resources/data/?noop=true&fileName=city.csv")
				.unmarshal("cityDataFormat").split(body()).to("direct:import");

		from("file:../jammazwan.shared/src/main/resources/data/?noop=true&fileName=company.csv")
				.unmarshal("companyDataFormat").split(body()).to("direct:import");

		from("file:../jammazwan.shared/src/main/resources/data/?noop=true&fileName=surname.txt")
				.unmarshal("surnameDataFormat").split(body()).to("direct:import");

		from("file:../jammazwan.shared/src/main/resources/data/?noop=true&fileName=name.csv")
				.unmarshal("nameDataFormat").split(body()).to("direct:import");

		from("direct:import").bean(xamBean);

		from("direct:doxml").bean(xamBean, "generate").split(body()).marshal().jacksonxml()
				.aggregate(header("CamelFileName"), new MyAggregationStrategy()).ignoreInvalidCorrelationKeys()
				.completionSize(25).completionTimeout(1000).to("file:.");
		
		from("direct:dojson").bean(xamBean, "generate").split(body()).marshal().json(JsonLibrary.Jackson)
				.aggregate(header("CamelFileName"), new MyAggregationStrategy()).ignoreInvalidCorrelationKeys()
				.completionSize(25).completionTimeout(1000).to("file:.");
	}

	public static class MyAggregationStrategy implements AggregationStrategy {

		public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
			if (oldExchange == null) {
				return newExchange;
			}
			String body1 = oldExchange.getIn().getBody(String.class);
			String body2 = newExchange.getIn().getBody(String.class);

			oldExchange.getIn().setBody(body1.trim() + "\n" + body2.trim());
			return oldExchange;
		}
	}
}
