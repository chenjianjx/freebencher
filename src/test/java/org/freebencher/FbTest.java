package org.freebencher;

import java.net.URL;

import org.junit.Test;

/**
 * 
 * @author chenjianjx@gmail.com
 *
 */
public class FbTest {

	@Test
	public void test() {

		FbRunner runner = new FbRunner();
		FbJob job = new FbJob();
		FbJobOptions options = new FbJobOptions();
		options.setConcurrency(5);
		options.setQuiet(true);
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

}
