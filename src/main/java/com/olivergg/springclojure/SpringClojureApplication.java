package com.olivergg.springclojure;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

@SpringBootApplication
public class SpringClojureApplication {

	private static final Log logger = LogFactory.getLog(SpringClojureApplication.class);

	public static void loadEdnResources() throws IOException {
		// ensure Clojure initialization is done. See
		// https://dev.clojure.org/jira/browse/CLJ-1172
		clojure.lang.RT.init();

		// Using Spring resolver here for convenient usage. The loading of resources
		// could be done without any Spring dependencies.
		PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();

		// a side effects consumer, that loads/compiles clojure code from the edn stored
		// in the given Resource.
		Consumer<? super Resource> action = rr -> {
			try (InputStreamReader isr = new InputStreamReader(rr.getInputStream())) {
				// the last two parameters of load are optional.
				Object obj = clojure.lang.Compiler.load(isr, rr.getURL().toString(), rr.getFilename());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		};

		// first load common.edn as it might be referenced by other edn file (there is
		// no magic dependency resolution).
		Resource common = r.getResources("**/com/olivergg/query/common.edn")[0];
		action.accept(common);

		// then load the other edn files.
		Stream.of(r.getResources("**/com/olivergg/query/*.edn")).filter(x -> !x.equals(common)).forEach(action);
	}

	/**
	 * Helper method to cast a clojure var to an IFN. The result of the invokatio should be cast to a String.
	 * @param ns
	 * @param name
	 * @return
	 */
	public static IFn query(String ns, String name) {
		return (IFn) Clojure.var(ns, name);
	}

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SpringClojureApplication.class, args);

		loadEdnResources();

		

		// building a simple query String
		String retclient = (String) query("com.olivergg.query.client", "myclientquery").invoke();
		logger.info("query string client = " + retclient);
		
		
		// one can pass arbitrary object to the invoke method.
		// here, we pass a boolean and a java.awt.Point.
		boolean param1 = true;
		java.awt.Point point = new java.awt.Point(1, 2);
		String retasset = (String) query("com.olivergg.query.asset", "myassetquery").invoke(param1, point);
		logger.info("query string asset = " + retasset);
		

	}

}
