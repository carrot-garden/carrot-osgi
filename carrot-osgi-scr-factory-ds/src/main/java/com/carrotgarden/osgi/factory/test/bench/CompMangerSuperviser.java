/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test.bench;

import java.util.Map;
import java.util.Properties;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class CompMangerSuperviser {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ComponentFactory factory;

	@Reference( //
	name = "reference", //
	target = "(component.factory=bench-manager)" //
	)
	protected void bind(final ComponentFactory s,
			final Map<String, Object> props) {

		factory = s;

		log.info("bind component.factory : {}", props.get("component.factory"));

	}

	protected void unbind(final ComponentFactory s) {

		log.info("unbind factory : {}", s);

		factory = null;

	}

	// @Reference
	protected void bindAny(final ComponentFactory s,
			final Map<String, Object> props) {

		log.info("bind component.factory : {}", props.get("component.factory"));

	}

	protected void unbindAny(final ComponentFactory s) {

		log.info("unbind factory : {}", s);

	}

	private ComponentContext context;

	@Activate
	protected void activate(final ComponentContext c) throws Exception {

		context = c;

		Thread.sleep(500);

		final Properties props = new Properties();

		props.put("reference.target", "(component.factory=bench.*)");

		final ComponentInstance instance = factory.newInstance(props);

	}

	@Deactivate
	protected void deactivate(final ComponentContext c) throws Exception {

		log.info("deactivate : {}", c);

		context = null;

	}

}
