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

		String reply = template.requestBodyAndHeader("direct:doxml", "No message here", "CamelFileName",
				"employees.xml", String.class);
		assertTrue(reply.startsWith("[City: Tokyo"));

		reply = template.requestBodyAndHeader("direct:dojson", "No message here", "CamelFileName", "employees.json",
				String.class);
		assertTrue(reply.startsWith("[City: Tokyo"));
	}

}
