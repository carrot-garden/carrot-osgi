/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.bean;

import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("properties")
public class PropertyFileBean implements BeanAcceptor,
		Comparable<PropertyFileBean> {

	@XStreamAsAttribute
	@XStreamAlias("entry")
	public String entry;

	//

	@Override
	public int compareTo(final PropertyFileBean that) {
		return this.entry.compareTo(that.entry);
	}

	@Override
	public int hashCode() {
		return entry.hashCode();
	}

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
