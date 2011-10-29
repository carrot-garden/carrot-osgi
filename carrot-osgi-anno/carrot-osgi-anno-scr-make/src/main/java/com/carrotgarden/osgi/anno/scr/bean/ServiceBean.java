package com.carrotgarden.osgi.anno.scr.bean;

import java.util.LinkedList;
import java.util.List;

import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("service")
public class ServiceBean implements BeanAcceptor {

	@XStreamAsAttribute
	@XStreamAlias("servicefactory")
	public boolean servicefactory = false;

	@XStreamImplicit(itemFieldName = "provide")
	public List<ProvideBean> provideList = new LinkedList<ProvideBean>();

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
