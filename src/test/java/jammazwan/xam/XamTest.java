package jammazwan.xam;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jammazwan.util.HoldContextOpenUntilDone;

public class XamTest extends CamelSpringTestSupport {

	@Override
	protected AbstractXmlApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
	}

	@Test
	public void testXam() throws Exception {
		HoldContextOpenUntilDone.go(context);

		String reply = template.requestBodyAndHeader("direct:doxml1", "No message here", "CamelFileName",
				"employees.xml", String.class);
		assertTrue(reply.startsWith("[City: Tokyo"));

		reply = template.requestBodyAndHeader("direct:dojson1", "No message here", "CamelFileName", "employees.json",
				String.class);
		assertTrue(reply.startsWith("[City: Tokyo"));

		reply = template.requestBodyAndHeader("direct:doxml2", "No message here", "CamelFileName", "shop.xml",
				String.class);
//		assertTrue(reply.startsWith("[City: Tokyo"));

		reply = template.requestBodyAndHeader("direct:dojson2", "No message here", "CamelFileName", "shop.json",
				String.class);
	}

}
