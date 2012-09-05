/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import com.carrotgarden.osgi.factory.test.Tidget;
import com.carrotgarden.osgi.factory.test.TidgetComponent1;
import com.carrotgarden.osgi.factory.test.TidgetComponent2;
import com.carrotgarden.osgi.factory.test.TidgetManager;

@RunWith(JUnit4TestRunner.class)
public class TestFactory extends TestAny {

	@Inject
	private TidgetManager manager;

	@Test
	public void testFactory1() throws Exception {

		final Map<String, String> propsIn = new HashMap<String, String>();

		final Tidget tidget = manager.create(TidgetComponent1.FACTORY, propsIn);

		assertNotNull(tidget);

		final Map<String, String> propsOut = tidget.getProps();

		log.debug("### propsOut : {}", propsOut);

		final Map<String, String> descriptor = manager
				.getFactoryDescriptor(TidgetComponent1.FACTORY);

		log.debug("### descriptor : {}", descriptor);

	}

	@Test
	public void testFactory2() throws Exception {

		final Map<String, String> propsIn = new HashMap<String, String>();

		final Tidget tidget = manager.create(TidgetComponent2.FACTORY, propsIn);

		assertNotNull(tidget);

		final Map<String, String> propsOut = tidget.getProps();

		log.debug("### propsOut : {}", propsOut);

		final Map<String, String> descriptor = manager
				.getFactoryDescriptor(TidgetComponent2.FACTORY);

		log.debug("### descriptor : {}", descriptor);

	}

}
