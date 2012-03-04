/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.bean;

import com.carrotgarden.osgi.anno.scr.conv.PropertyBeanConverter;
import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("property")
@XStreamConverter(PropertyBeanConverter.class)
public class PropertyBean implements BeanAcceptor, Comparable<PropertyBean> {

	public String name;

	public String type;

	public String value;

	//

	@Override
	public int compareTo(final PropertyBean that) {
		return this.name.compareTo(that.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof PropertyBean) {
			final PropertyBean that = (PropertyBean) other;
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
