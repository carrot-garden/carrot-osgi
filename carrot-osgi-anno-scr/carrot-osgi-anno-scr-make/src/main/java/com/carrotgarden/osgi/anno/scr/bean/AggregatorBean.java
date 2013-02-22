/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.bean;

import java.util.LinkedList;
import java.util.List;

import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("container")
public class AggregatorBean implements BeanAcceptor {

	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	public String xmlns = "http://www.osgi.org/xmlns/scr/v1.1.0";

	@XStreamImplicit(itemFieldName = "component")
	public List<ComponentBean> componentList = new LinkedList<ComponentBean>();

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
