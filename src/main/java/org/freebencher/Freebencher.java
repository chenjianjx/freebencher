package org.freebencher;

/**
 * The most used operations. If you need advanced ones, see {@link FbRunner} 
 * @author chenjianjx@gmail.com
 *
 */
public class Freebencher {
	
	public static FbJobResult benchmark(FbTarget target, int concurrency, int numOfTests){
		FbRunner runner = new FbRunner();
		FbJob job = new FbJob();
		
		FbJobOptions options = new FbJobOptions();
		options.setConcurrency(concurrency);		
		options.setNumOfTests(numOfTests);
		job.setOptions(options);
		
		job.setTarget(target);
		
		runner.run(job);
		
		return job.getResult();
	}

}
