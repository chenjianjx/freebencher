package org.freebencher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Heavily inspired by apache ab
 * 
 * @author chenjianjx@gmail.com
 *
 */
public class FbJobResult {

	private int concurrency;
	/**
	 * unit: ms
	 */
	private long timeTakenForTests;

	/**
	 * successful tests
	 */
	private AtomicInteger successfulTests = new AtomicInteger();

	/**
	 * failed tests
	 */
	private AtomicInteger failedTests = new AtomicInteger();

	private CopyOnWriteArrayList<FbSingleTestResult> singleTestResults = new CopyOnWriteArrayList<FbSingleTestResult>();

	protected static class FbSingleTestResult implements Comparable<FbSingleTestResult> {
		private boolean successful;
		private long timeTaken;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}

		public boolean isSuccessful() {
			return successful;
		}

		public void setSuccessful(boolean successful) {
			this.successful = successful;
		}

		public long getTimeTaken() {
			return timeTaken;
		}

		public void setTimeTaken(long timeTaken) {
			this.timeTaken = timeTaken;
		}

		/**
		 * sort by time taken
		 */
		@Override
		public int compareTo(FbSingleTestResult o) {
			return new Long(timeTaken).compareTo(new Long(o.getTimeTaken()));
		}

	}

	public void addSingleTestResult(boolean successful, long timeTaken) {
		FbSingleTestResult singleResult = new FbSingleTestResult();
		singleResult.setSuccessful(successful);
		singleResult.setTimeTaken(timeTaken);
		singleTestResults.add(singleResult);
	}

	public CopyOnWriteArrayList<FbSingleTestResult> getSingleTestResults() {
		return singleTestResults;
	}

	/**
	 * the total tests
	 * 
	 * @return
	 */
	public int getNumOfTests() {
		return successfulTests.intValue() + failedTests.intValue();
	}

	/**
	 * the avg time of each test
	 * 
	 * @return
	 */
	public double getMeanTimePerTest() {
		long sum = 0;
		for (FbSingleTestResult single : singleTestResults) {
			sum += single.getTimeTaken();
		}
		return ((double) sum) / singleTestResults.size();
	}

	/**
	 * something like: 90% of tests done in 3 ms, 95% done in 4 ms
	 * 
	 * @return
	 */
	public TreeMap<Double, Long> getPercentInCertainTime() {
		List<FbSingleTestResult> sortedResults = new ArrayList<FbSingleTestResult>(singleTestResults);
		Collections.sort(sortedResults);
		TreeMap<Double, Long> map = new TreeMap<Double, Long>();
		fillPercentN(map, sortedResults, 0.5);
		fillPercentN(map, sortedResults, 0.6);
		fillPercentN(map, sortedResults, 0.7);
		fillPercentN(map, sortedResults, 0.8);
		fillPercentN(map, sortedResults, 0.9);
		fillPercentN(map, sortedResults, 0.95);
		fillPercentN(map, sortedResults, 0.98);
		fillPercentN(map, sortedResults, 0.99);
		fillPercentN(map, sortedResults, 1);
		return map;
	}

	private void fillPercentN(TreeMap<Double, Long> map, List<FbSingleTestResult> sortedResults, double percent) {
		map.put(percent, getTimeTakenOfNthSingleResult(sortedResults, percent));
	}

	private long getTimeTakenOfNthSingleResult(List<FbSingleTestResult> sortedResults, double percent) {
		int length = sortedResults.size();
		int indexOneBased = (int) Math.round(length * percent);
		long timeTaken = sortedResults.get(indexOneBased - 1).getTimeTaken();
		return timeTaken;
	}

	public int getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(int concurrencyLevel) {
		this.concurrency = concurrencyLevel;
	}

	public long getTimeTakenForTests() {
		return timeTakenForTests;
	}

	public void setTimeTakenForTests(long timeTakenForTests) {
		this.timeTakenForTests = timeTakenForTests;
	}

	public AtomicInteger getSuccessfulTests() {
		return successfulTests;
	}

	public void setSuccessfulTests(AtomicInteger completeTests) {
		this.successfulTests = completeTests;
	}

	public AtomicInteger getFailedTests() {
		return failedTests;
	}

	public void setFailedTests(AtomicInteger failedTests) {
		this.failedTests = failedTests;
	}

	/**
	 * the throughput. If you are doing http test, this is the QPS. However, if
	 * preInvoke() is used, this values doesn't make sense
	 */
	public double getTestsPerSecond() {

		return (double) getNumOfTests() * 1000 / this.getTimeTakenForTests();
	}

	public String report() {
		StringBuffer report = new StringBuffer();
		int vfs = 25;
		report.append(reportLine("Concurrency", this.getConcurrency(), vfs)).append("\n");
		report.append(
				reportLine("Time taken for tests (including pre-invoke)", this.getTimeTakenForTests() + "ms", vfs))
				.append("\n");
		report.append(reportLine("Successful tests", this.getSuccessfulTests(), vfs)).append("\n");
		report.append(reportLine("Failed tests", this.getFailedTests(), vfs)).append("\n");
		report.append(reportLine("Tests per second\n(Doesn't make sense if preInvoke() is used)",
				this.getTestsPerSecond(), vfs)).append("\n");
		report.append(reportLine("Mean time per test", this.getMeanTimePerTest() + "ms", vfs)).append("\n");

		report.append("Percentage of the test finished within a certain time (ms)\n");
		TreeMap<Double, Long> percentInTime = this.getPercentInCertainTime();
		for (Map.Entry<Double, Long> entry : percentInTime.entrySet()) {
			Double key = entry.getKey();
			Long value = entry.getValue();
			report.append(reportLine(toPercentage(key), value, vfs)).append("\n");
		}

		return report.toString();
	}

	private static String toPercentage(double n) {
		return String.format("%.0f", n * 100) + "%";
	}

	private String reportLine(String key, Object value, int valueOffset) {
		StringBuffer sb = new StringBuffer();
		String leftColumn = key + ":";
		sb.append(leftColumn);
		for (int i = 1; i <= valueOffset - leftColumn.length(); i++) {
			sb.append(" ");
		}
		sb.append(value);
		return sb.toString();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
