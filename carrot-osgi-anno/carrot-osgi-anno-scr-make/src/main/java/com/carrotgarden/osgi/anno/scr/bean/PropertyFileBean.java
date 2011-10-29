package com.carrotgarden.osgi.anno.scr.bean;

import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("properties")
public class PropertyFileBean implements BeanAcceptor {

	@XStreamAsAttribute
	@XStreamAlias("entry")
	public String entry;

	@Override
	public boolean equals(final Object other) {
		if (other instanceof PropertyFileBean) {
			final PropertyFileBean that = (PropertyFileBean) other;
			return this.entry.equals(that.entry);
		}
		return false;
	}

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
