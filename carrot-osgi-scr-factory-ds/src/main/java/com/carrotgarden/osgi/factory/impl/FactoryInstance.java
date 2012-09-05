/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

/** holder of {@link ComponentFactory} properties */
class FactoryInstance {

	final String factoryId;

	final Class<?> fidgetClass;

	final Map<String, String> factoryDescriptor;

	final FactoryContext factoryContext;

	FactoryInstance(final String factoryId, final Class<?> fidgetClass,
			final Map<String, String> factoryDescriptor,
			final FactoryContext factoryContext) {

		this.factoryId = factoryId;

		this.fidgetClass = fidgetClass;

		this.factoryDescriptor = factoryDescriptor;

		this.factoryContext = factoryContext;

	}

	ComponentFactory factoryOSGI() {
		return factoryContext.factoryOSGI;
	}

	ComponentInstance newInstance(final Map<String, String> props) {

		final Dictionary<String, String> dict;

		if (props == null) {
			dict = null;
		} else {
			dict = new Hashtable<String, String>(props);
		}

		final ComponentInstance wrapper = factoryOSGI().newInstance(dict);

		return wrapper;

	}

	@Override
	public String toString() {

		final StringBuilder text = new StringBuilder(128);

		text.append("id=");
		text.append(factoryId);
		text.append("; ");

		text.append("descriptor=");
		text.append(factoryDescriptor);
		text.append("; ");

		text.append("component=");
		text.append(fidgetClass.getName());
		text.append("; ");

		return text.toString();

	}

}
