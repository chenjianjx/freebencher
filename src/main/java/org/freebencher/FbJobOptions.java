package org.freebencher;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Test Options. Inspired by Apache AB
 * 
 * @author chenjianjx@gmail.com
 *
 */
public class FbJobOptions {

	private int concurrency;

	/**
	 * the total number of tests
	 */
	private int numOfTests;

	/**
	 * When processing more than 150 tests, fb outputs a progress count on stderr
	 * every 10% or 100 requests or so. This flag will suppress these messages.
	 */
	private boolean quiet;

	public static FbJobOptions defaultOptions() {
		FbJobOptions options = new FbJobOptions();
		options.setConcurrency(1);
		options.setNumOfTests(1);
		return options;
	}

	public int getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(int concurrency) {
		this.concurrency = concurrency;
	}

	public int getNumOfTests() {
		return numOfTests;
	}

	public void setNumOfTests(int tests) {
		this.numOfTests = tests;
	}

	public boolean isQuiet() {
		return quiet;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
