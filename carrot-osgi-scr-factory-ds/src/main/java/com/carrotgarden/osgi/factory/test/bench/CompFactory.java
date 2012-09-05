/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test.bench;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(factory = CompFactory.FACTORY)
public class CompFactory implements Comp {

	public static final String FACTORY = "bench.factory";

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Property(name = "important-property")
	static final String VALUE = "important property value";

}
