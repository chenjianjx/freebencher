package org.freebencher;

import static org.freebencher.util.FbUtils.assertNotNull;
import static org.freebencher.util.FbUtils.assertPositive;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.time.StopWatch;

/**
 * run a test
 * 
 * @author chenjianjx@gmail.com
 *
 */
public class FbRunner {

	public void run(final FbJob job) {
		// validate and init data
		assertNotNull(job, "job cannot be null");
		assertNotNull(job.getTarget(),
				"the test target cannot be null. What you want to test? ");
		if (job.getOptions() == null) {
			job.setOptions(FbJobOptions.defaultOptions());
		}
		assertPositive(job.getOptions().getConcurrency(),
				"concurrency should be > 0");
		assertPositive(job.getOptions().getNumOfTests(),
				"numOfTests should be > 0");
		job.setResult(new FbJobResult());

		// create a thread pool
		FbJobOptions options = job.getOptions();
		FbJobResult result = job.getResult();
		ExecutorService threadPool = Executors.newFixedThreadPool(
				options.getConcurrency(), new FbThreadFactory());

		System.out.println("Test started.");

		// start working
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 1; i <= options.getNumOfTests(); i++) {
			FbCall call = new FbCall(job);
			threadPool.submit(call);
		}

		try {
			threadPool.shutdown();
			System.out.println("Awaiting termination...");
			threadPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// foreget about it
		}
		stopWatch.stop();
		System.out.println("Test completed.");

		// fill results
		result.setTimeTakenForTests(stopWatch.getTime());
		result.setConcurrency(options.getConcurrency());
	}

	private static class FbCall implements Callable<Void> {
		private FbJob job;

		public FbCall(FbJob job) {
			this.job = job;
		}

		@Override
		public Void call() throws Exception {
			try {
				doIt();
			} catch (Exception e) {
				System.err.print("Failed to invoke a single test. ");
				e.printStackTrace();
			}
			return null;
		}

		private void doIt() {
			boolean successful = false;

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			try {
				successful = job.getTarget().invoke();
			} catch (Exception e) {
				successful = false;
			}
			stopWatch.stop();
			if (successful) {
				job.getResult().getSuccessfulTests().incrementAndGet();
			} else {
				job.getResult().getFailedTests().incrementAndGet();
			}

			job.getResult()
					.addSingleTestResult(successful, stopWatch.getTime());
			int results = job.getResult().getNumOfTests();
			if (results != 0 && !job.getOptions().isQuiet()
					&& (job.getResult().getNumOfTests() % 100 == 0)) {
				System.err.printf("%d/%d are done\n", results, job.getOptions()
						.getNumOfTests());
			}
		}

	}

	/**
	 * copied from jdk's source code
	 * 
	 * @author chenjianjx@gmail.com
	 *
	 */
	private static class FbThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		private FbThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
					.getThreadGroup();
			namePrefix = "fb-runnner-pool-" + poolNumber.getAndIncrement()
					+ "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix
					+ threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
