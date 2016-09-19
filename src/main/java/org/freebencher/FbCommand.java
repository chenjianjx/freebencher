package org.freebencher;

import java.util.Map;

/**
 * The java code block to test. It's a stateless singleton object shared by all
 * the threads that will be spawned in a single job. If you want each invocation
 * has its own status, put the state into the 'context' parameters in the
 * methods below
 * 
 * @author chenjianjx@gmail.com
 *
 */
@SuppressWarnings("deprecation")
public abstract class FbCommand implements FbTarget{
	/**
	 * put the behavior you want to test inside this callback method.
	 * 
	 * @param context
	 *            Will not be shared contexts
	 * @return successful?
	 */
	public abstract boolean invoke(Map<String, Object> context);

	/**
	 * Before you do the real test, you may want to do some preparation work. A
	 * typical usage is: You need to test B, but you must run A first to
	 * construct the parameters for B. Then you can put A's code under this
	 * method, build the parameter and save it into this method's 'context'
	 * parameter, which can be retrieved by {@link #invoke(Map)} later. <br/>
	 * 
	 * Note that execution time in this method will not be calculated into
	 * response time.
	 * 
	 * @param context
	 *            Will not be shared contexts
	 * @return successful? if yes, then go on with {@link #invoke(Map)};
	 *         otherwise, this single run will be cancelled (test neither success nor failed). 
	 */
	public abstract boolean preInvoke(Map<String, Object> context);
	
	/**
	 * This method will never be run
	 */
	public boolean invoke(){
		throw new IllegalStateException();
	}
	
}
