package org.freebencher;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A job made up of a number of concurrent tests
 * 
 * @author chenjianjx@gmail.com
 *
 */
@SuppressWarnings("deprecation")
public class FbJob {

	private FbTarget target;

	private FbJobOptions options;

	private FbJobResult result;

	public FbTarget getTarget() {
		return target;
	}

	@Deprecated
	public void setTarget(FbTarget target) {
		this.target = target;
	}

	public void setCommand(FbCommand command) {
		this.target = command;
	}

	public FbJobOptions getOptions() {
		return options;
	}

	public void setOptions(FbJobOptions options) {
		this.options = options;
	}

	public FbJobResult getResult() {
		return result;
	}

	public void setResult(FbJobResult result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
