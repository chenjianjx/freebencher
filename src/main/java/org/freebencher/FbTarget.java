package org.freebencher;

/**
 * The java code block to test
 * 
 * @author chenjianjx@gmail.com
 * @deprecated Use {@link FbCommand} instead
 */
public interface FbTarget {
	/**
	 * put the code inside this callback method. If you have any parameters,
	 * please set them as member variables
	 * 
	 * @return successful?
	 */
	public boolean invoke();
}