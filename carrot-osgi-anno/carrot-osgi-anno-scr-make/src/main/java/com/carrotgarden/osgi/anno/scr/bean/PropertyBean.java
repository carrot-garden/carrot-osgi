package com.carrotgarden.osgi.anno.scr.bean;

import com.carrotgarden.osgi.anno.scr.conv.PropertyBeanConverter;
import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("property")
@XStreamConverter(PropertyBeanConverter.class)
public class PropertyBean implements BeanAcceptor {

	public String name;

	public String type;

	public String value;

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		// TODO Auto-generated method stub
	}

}
