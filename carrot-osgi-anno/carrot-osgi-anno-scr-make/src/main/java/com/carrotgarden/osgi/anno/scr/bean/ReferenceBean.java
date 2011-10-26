package com.carrotgarden.osgi.anno.scr.bean;

import java.lang.reflect.Method;

import org.osgi.service.component.annotations.Reference;
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
	public String name;

	@XStreamAsAttribute
	@XStreamAlias("interface")
	public String type;

	@XStreamAsAttribute
	@XStreamAlias("cardinality")
	@XStreamConverter(ReferenceCardinalityConverter.class)
	public ReferenceCardinality cardinality;

	@XStreamAsAttribute
	@XStreamAlias("policy")
	@XStreamConverter(ReferencePolicyConverter.class)
	public ReferencePolicy policy;

	@XStreamAsAttribute
	@XStreamAlias("target")
	public String target;

	@XStreamAsAttribute
	@XStreamAlias("bind")
	public String bind;

	@XStreamAsAttribute
	@XStreamAlias("unbind")
	public String unbind;

	//

	public void apply(final Reference anno, final Method method) {

		final String bindName = method.getName();

		final String unbindName = "un" + bindName;

		String temp = bindName;
		temp = temp.replaceFirst("add", "");
		temp = temp.replaceFirst("set", "");
		temp = temp.replaceFirst("bind", "");

		//

		name = Util.isValid(anno.name()) ? anno.name() : temp;

		final Class<?>[] paramArary = method.getParameterTypes();

		type = paramArary[0].getName();

		cardinality = anno.cardinality();

		policy = anno.policy();

		target = Util.isValid(anno.target()) ? anno.target() : null;

		bind = bindName;

		unbind = unbindName;

	}

}
