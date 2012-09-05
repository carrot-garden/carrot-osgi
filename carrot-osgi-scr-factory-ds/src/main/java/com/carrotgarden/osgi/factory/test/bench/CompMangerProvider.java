/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test.bench;

import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(factory = CompMangerProvider.FACTORY)
public class CompMangerProvider implements CompManager {

	public static final String FACTORY = "bench-manager";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ComponentFactory factory;

	@Reference( //
	name = "reference", //
	target = "(component.factory=none)", //
	cardinality = ReferenceCardinality.MULTIPLE, //
	policy = ReferencePolicy.DYNAMIC //
	)
	protected void bind(final ComponentFactory s,
			final Map<String, Object> props) {

		factory = s;

		log.info("bind @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		log.info("bind factory   : {}", factory);
		log.info("bind props     : {}", props);

		log.info("bind component.factory : {}", props.get("component.factory"));
		log.info("bind component.name    : {}", props.get("component.name"));

		log.info("bind @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

	}

	protected void unbind(final ComponentFactory s) {

		log.info("unbind factory : {}", s);

		factory = null;

	}

	private ComponentContext context;

	@Activate
	protected void activate(final ComponentContext c) {

		context = c;

		log.info("activate props   : {}", context.getProperties());
		log.info("activate factory : {}", factory);

	}

	@Deactivate
	protected void deactivate(final ComponentContext c) {

		log.info("deactivate : {}", c);

		context = null;

	}

}
