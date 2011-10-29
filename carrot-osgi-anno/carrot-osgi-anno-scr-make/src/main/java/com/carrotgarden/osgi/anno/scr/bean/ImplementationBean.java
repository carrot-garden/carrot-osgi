package com.carrotgarden.osgi.anno.scr.bean;

import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("implementation")
public class ImplementationBean implements BeanAcceptor {

	@XStreamAsAttribute
	@XStreamAlias("class")
	public String klaz;

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
