/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf.test;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  */
@Component( //
service = TestSwitcher.class, //
name = TestSwitcher.NAME //
)
public class TestSwitcher implements TestService {

	public final static String NAME = "SWITCH";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ComponentContext context;

	public void enable(final String componentName) {
		context.enableComponent(componentName);
	}

	public void disable(final String componentName) {
		context.disableComponent(componentName);
	}

	@Activate
	protected void activate(final ComponentContext c) {

		context = c;

		log.debug("activate : {}", c);

	}

	@Modified
	protected void modified(final ComponentContext c) {

		log.debug("modified : {}", c);

	}

	@Deactivate
	protected void deactivate(final ComponentContext c) {

		log.debug("deactivate : {}", c);

	}

}
