/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.bean;

import java.lang.reflect.Method;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.carrotgarden.osgi.anno.scr.conv.ReferenceCardinalityConverter;
import com.carrotgarden.osgi.anno.scr.conv.ReferencePolicyConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("reference")
public class ReferenceBean {

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
	public boolean equals(final Object other) {
		if (other instanceof ReferenceBean) {
			final ReferenceBean that = (ReferenceBean) other;
			return this.name.equals(that.name);
		}
		return false;
	}

	//

	public void apply(final Class<?> klaz, final Method bindMethod,
			final Reference reference) {

		final String bindName = bindMethod.getName();

		final Class<?> bindType = Util.bindType(bindMethod);

		//

		type = bindType.getName();

		target = Util.isValid(reference.target()) ? reference.target() : null;

		name = Util.isValid(reference.name()) ? reference.name() : type + "/" + target;

		cardinality = reference.cardinality();

		policy = reference.policy();

		bind = bindName;

		unbind = Util.unbindName(bindName);

		//

		Util.assertBindUnbind(klaz, bindType, bind, unbind);

	}

}
