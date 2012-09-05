/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import com.carrotgarden.osgi.factory.api.Cidget;
import com.carrotgarden.osgi.factory.test.Tidget;
import com.carrotgarden.osgi.factory.test.TidgetComp1;
import com.carrotgarden.osgi.factory.test.TidgetComp2;
import com.carrotgarden.osgi.factory.test.TidgetManager;
import com.carrotgarden.osgi.factory.test.TidgetSwitch;

@RunWith(JUnit4TestRunner.class)
public class TestFactoryCross extends TestAny {

	@Inject
	protected TidgetManager manager;

	@Inject
	protected TidgetSwitch switcher;

	/** ensure no cross leaks */
	@Test
	public void testFactory2() throws Exception {

		final String instance1 = "reader-001";
		final String instance2 = "reader-002";

		final Map<String, String> props1 = new HashMap<String, String>();
		props1.put(Cidget.PROP_INSTANCE_ID, instance1);

		final Map<String, String> props2 = new HashMap<String, String>();
		props2.put(Cidget.PROP_INSTANCE_ID, instance2);

		assertEquals(0, manager.instanceList().size());

		final Tidget service1 = manager.instanceCreate(TidgetComp1.NAME, props1);

		assertNotNull("got service 1", service1);

		assertTrue(service1 == manager.instance(service1.instanceId()));

		final Tidget service2 = manager.instanceCreate(TidgetComp2.NAME, props2);

		assertNotNull("got service 2", service2);

		assertTrue(service2 == manager.instance(service2.instanceId()));

		assertTrue(service1 != service2);

		assertEquals(2, manager.instanceList().size());
		assertTrue(manager.instanceList().contains(service1));
		assertTrue(manager.instanceList().contains(service2));

		manager.instanceDestroy(service1.instanceId());
		Thread.sleep(200);

		assertEquals(1, manager.instanceList().size());
		assertFalse(manager.instanceList().contains(service1));
		assertTrue(manager.instanceList().contains(service2));

		manager.instanceDestroy(service2.instanceId());
		Thread.sleep(200);

		assertEquals(0, manager.instanceList().size());
		assertFalse(manager.instanceList().contains(service1));
		assertFalse(manager.instanceList().contains(service2));

		/** wait for logging pretty-print */
		Thread.sleep(100);

	}

}
