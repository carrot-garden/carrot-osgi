/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.Map;

import org.osgi.service.component.ComponentFactory;

/** {@link ComponentFactory} bind/unbind context */
class FactoryContext {

	final ComponentFactory factoryOSGI;

	final Map<String, String> factoryProps;

	FactoryContext(final ComponentFactory factoryOSGI,
			final Map<String, String> factoryProps) {

		this.factoryOSGI = factoryOSGI;

		this.factoryProps = factoryProps;

	}

}
