/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.bean;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("container")
public class AggregatorBean {

	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	public String xmlns = "http://www.osgi.org/xmlns/scr/v1.1.0";

	@XStreamImplicit(itemFieldName = "component")
	public List<ComponentBean> componentList = new LinkedList<ComponentBean>();

	//

	private void applyClass(final Class<?> klaz) {

		final ComponentBean bean = new ComponentBean();

		final List<Class<?>> list = Util.getClassList(klaz);

		for (final Class<?> type : list) {

			bean.apply(type);

		}

		componentList.add(bean);

	}

	public void apply(final Class<?>... klazArray) {

		for (final Class<?> klaz : klazArray) {

			if (Util.isAbstract(klaz)) {
				continue;
			}

			if (!Util.isComponent(klaz)) {
				continue;
			}

			applyClass(klaz);

		}

	}

}
