/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class TidgetSwitchProvider implements TidgetSwitch {

	@Override
	public void enable(final String componentName) {
		context.enableComponent(componentName);
	}

	@Override
	public void disable(final String componentName) {
		context.disableComponent(componentName);
	}

	private ComponentContext context;

	@Activate
	protected void activate(final ComponentContext c) {
		context = c;
	}

	@Deactivate
	protected void deactivate(final ComponentContext c) {
		context = null;
	}

}
