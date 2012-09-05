/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.Map;

import com.carrotgarden.osgi.factory.api.Fidget;

/** unwrap {@link Fidget} properties form a {@link FactoryContext} */
public interface FactoryHelper {

	String factoryId(FactoryContext context);

	Class<? extends Fidget> fidgetClass(FactoryContext context)
			throws Exception;

	Map<String, String> fidgetDescriptor(FactoryContext context)
			throws Exception;

	void logFactoryProps(FactoryContext context);

}
