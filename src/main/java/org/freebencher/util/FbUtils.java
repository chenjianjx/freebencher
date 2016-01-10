package org.freebencher.util;

/**
 * 
 * @author chenjianjx@gmail.com
 *
 */
public class FbUtils {

	public static void assertNotNull(Object o, String err) {
		if (o == null) {
			throw new RuntimeException(err);
		}
	}

	public static void assertPositive(int num, String err) {
		if (num <= 0) {
			throw new RuntimeException(err);
		}

	}
}
