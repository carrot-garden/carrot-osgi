package com.carrotgarden.osgi.anno.scr.bean;

import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.carrotgarden.osgi.anno.scr.conv.ReferenceCardinalityConverter;
import com.carrotgarden.osgi.anno.scr.conv.ReferencePolicyConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("reference")
public class ReferenceBean {

	@XStreamAsAttribute
	@XStreamAlias("name")
	public String name = "";

	@XStreamAsAttribute
	@XStreamAlias("interface")
	public String klaz = "";

	@XStreamAsAttribute
	@XStreamAlias("cardinality")
	@XStreamConverter(ReferenceCardinalityConverter.class)
	public ReferenceCardinality cardinality = ReferenceCardinality.MANDATORY;

	@XStreamAsAttribute
	@XStreamAlias("policy")
	@XStreamConverter(ReferencePolicyConverter.class)
	public ReferencePolicy policy = ReferencePolicy.STATIC;

	@XStreamAsAttribute
	@XStreamAlias("target")
	public String target = "";

	@XStreamAsAttribute
	@XStreamAlias("bind")
	public String bind = "";

	@XStreamAsAttribute
	@XStreamAlias("unbind")
	public String unbind = "";

}
