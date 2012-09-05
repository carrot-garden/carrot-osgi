/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf.test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** example of a singleton, disabled by default, not waiting on config */
@Component(//
enabled = false, //
immediate = true, //
name = TestCompSingle1.NAME)
public class TestCompSingle1 implements TestService {

	public final static String NAME = "SINGLE-ONE";

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static final AtomicInteger count = new AtomicInteger(0);

	@Activate
	protected void activate(final Map<String, String> props) {

		count.getAndIncrement();

		log.debug("activate : {}", props);

	}

	@Modified
	protected void modified(final Map<String, String> props) {

		log.debug("modified : {}", props);

	}

	@Deactivate
	protected void deactivate(final Map<String, String> props) {

		log.debug("deactivate : {}", props);

		count.getAndDecrement();

	}

}
