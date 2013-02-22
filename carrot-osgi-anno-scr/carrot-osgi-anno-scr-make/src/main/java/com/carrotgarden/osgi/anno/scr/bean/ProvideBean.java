/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
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

/**
 * Provided component interfaces tag.
 */
@XStreamAlias("provide")
public class ProvideBean implements BeanAcceptor, Comparable<ProvideBean> {

	@XStreamAsAttribute
	@XStreamAlias("interface")
	public String type;

	//

	@Override
	public int compareTo(final ProvideBean that) {
		return this.type.compareTo(that.type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof ProvideBean) {
			final ProvideBean that = (ProvideBean) other;
			return this.type.equals(that.type);
		}
		return false;
	}

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
