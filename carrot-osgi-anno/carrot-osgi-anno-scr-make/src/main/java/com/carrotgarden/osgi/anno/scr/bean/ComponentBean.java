package com.carrotgarden.osgi.anno.scr.bean;

import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.carrotgarden.osgi.anno.scr.conv.ConfigurationPolicyConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("component")
public class ComponentBean {

	@XStreamAsAttribute
	@XStreamAlias("name")
	public String name = "";

	@XStreamAsAttribute
	@XStreamAlias("enabled")
	public boolean enabled = true;

	@XStreamAsAttribute
	@XStreamAlias("factory")
	public String factory = "";

	@XStreamAsAttribute
	@XStreamAlias("immediate")
	public boolean immediate = true;

	@XStreamAsAttribute
	@XStreamAlias("configuration-policy")
	@XStreamConverter(ConfigurationPolicyConverter.class)
	public ConfigurationPolicy policy = ConfigurationPolicy.OPTIONAL;

	@XStreamAsAttribute
	@XStreamAlias("activate")
	public String activate = "activate";

	@XStreamAsAttribute
	@XStreamAlias("deactivate")
	public String deactivate = "deactivate";

	@XStreamAsAttribute
	@XStreamAlias("modified")
	public String modified = "modified";

	//

	public ImplementationBean implementation = new ImplementationBean();

	public ServiceBean service = new ServiceBean();

	public ReferenceBean reference = new ReferenceBean();

}
