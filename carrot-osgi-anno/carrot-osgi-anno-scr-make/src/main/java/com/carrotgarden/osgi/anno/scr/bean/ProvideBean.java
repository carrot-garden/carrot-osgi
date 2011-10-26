package com.carrotgarden.osgi.anno.scr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("provide")
public class ProvideBean {

	@XStreamAsAttribute
	@XStreamAlias("interface")
	public String klaz = "";

	@Override
	public boolean equals(final Object other) {
		if (other instanceof ProvideBean) {
			final ProvideBean that = (ProvideBean) other;
			return this.klaz.equals(that.klaz);
		}
		return false;
	}

}
