package com.carrotgarden.osgi.anno.scr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("provide")
public class ProvideBean {

	@XStreamAsAttribute
	@XStreamAlias("interface")
	public String klaz = "";

}
