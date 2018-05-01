package com.olivergg.springclojure;

import java.io.IOException;
import static com.olivergg.springclojure.SpringClojureApplication.*;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringClojureApplicationTests {

	@BeforeClass
	public static void before() throws IOException {
		loadEdnResources();
	}

	@Test
	public void testQuery() {
		boolean param1 = false;
		java.awt.Point point = new java.awt.Point(1, 2);
		String retasset = (String) query("com.olivergg.query.asset", "myassetquery").invoke(param1, point);
		assertEquals("select {c.*} from asset a         where a.name = :namedParameter1  and a.xpos = :xpoint order by a.description",
				retasset.trim());
	}

}
