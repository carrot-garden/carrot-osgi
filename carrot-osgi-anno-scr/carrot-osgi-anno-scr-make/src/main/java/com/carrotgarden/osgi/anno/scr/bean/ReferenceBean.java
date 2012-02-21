package com.carrotgarden.osgi.anno.scr.bean;

import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.carrotgarden.osgi.anno.scr.conv.ReferenceCardinalityConverter;
import com.carrotgarden.osgi.anno.scr.conv.ReferencePolicyConverter;
import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("reference")
public class ReferenceBean implements BeanAcceptor, Comparable<ReferenceBean> {

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

	@Override
	public int compareTo(final ReferenceBean that) {
		return this.name.compareTo(that.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof ReferenceBean) {
			final ReferenceBean that = (ReferenceBean) other;
			return this.name.equals(that.name);
		}
		return false;
	}

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
