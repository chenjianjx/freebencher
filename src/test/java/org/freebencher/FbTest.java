package org.freebencher;

import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

/**
 * 
 * @author chenjianjx@gmail.com
 *
 */
@SuppressWarnings("deprecation")
public class FbTest {

	@Test
	public void test() {

		FbRunner runner = new FbRunner();
		FbJob job = new FbJob();
		FbJobOptions options = new FbJobOptions();
		options.setConcurrency(5);
		//options.setQuiet(true);
		options.setNumOfTests(222);
		job.setOptions(options);
		FbTarget target = new FbTarget() {
			@Override
			public boolean invoke() {

				try {
					URL url = new URL("http://global.bing.com/search?q=hello");
					url.openConnection().getContent();
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};
		job.setTarget(target);
		runner.run(job);
		System.out.println(job.getResult().report());

	}
	
	
	@Test
	public void testWithFbCommand() {

		FbRunner runner = new FbRunner();
		FbJob job = new FbJob();
		FbJobOptions options = new FbJobOptions();
		options.setConcurrency(5);
		//options.setQuiet(true);
		options.setNumOfTests(222);
		job.setOptions(options);		 
		FbCommand target = new FbCommand() {
 

			@Override
			public boolean invoke(Map<String, Object> context) {
				try {
					URL url = new URL("http://global.bing.com/search?q=" + context.get("keyword"));
					url.openConnection().getContent();
					return true;
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public boolean preInvoke(Map<String, Object> context) {
				context.put("keyword", "hello");
				return RandomUtils.nextBoolean();
			}
		};
		job.setTarget(target);
		runner.run(job);
		System.out.println(job.getResult().report());

	}	
	

}
