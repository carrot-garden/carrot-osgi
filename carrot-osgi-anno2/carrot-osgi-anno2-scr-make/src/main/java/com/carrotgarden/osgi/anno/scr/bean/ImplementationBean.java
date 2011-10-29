package com.carrotgarden.osgi.anno.scr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("implementation")
public class ImplementationBean {

	@XStreamAsAttribute
	@XStreamAlias("class")
	public String klaz = "";

}
