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

import com.carrotgarden.osgi.factory.api.Cidget;
import com.carrotgarden.osgi.factory.test.Tidget;
import com.carrotgarden.osgi.factory.test.TidgetComp1;
import com.carrotgarden.osgi.factory.test.TidgetDependencyImpl;
import com.carrotgarden.osgi.factory.test.TidgetManager;
import com.carrotgarden.osgi.factory.test.TidgetSwitch;

@RunWith(JUnit4TestRunner.class)
public class TestFactoryDeps extends TestAny {

	@Inject
	protected TidgetManager manager;

	@Inject
	protected TidgetSwitch switcher;

	/**
	 * dependency enable / disable will re-create instance for given
	 * configuration
	 */
	@Test
	public void testFactory0() throws Exception {

		final String instanceId = "reader-001";

		assertFalse("must not have", manager.hasConfig(instanceId));

		final Map<String, String> props = new HashMap<String, String>();

		props.put(Cidget.PROP_INSTANCE_ID, instanceId);

		assertEquals(0, TidgetComp1.countActivate);
		assertEquals(0, TidgetComp1.countModified);
		assertEquals(0, TidgetComp1.countDeactivate);

		final Tidget service1 = manager.instanceCreate(TidgetComp1.NAME, props);

		assertNotNull("got service 1", service1);

		assertEquals(1, TidgetComp1.countActivate);

		assertTrue(service1.isActive());
		assertEquals(1, manager.instanceList().size());
		assertTrue(manager.instanceList().contains(service1));
		assertTrue(manager.hasInstance(service1.instanceId()));

		switcher.disable(TidgetDependencyImpl.NAME);
		Thread.sleep(400);

		assertTrue("must have", manager.hasConfig(instanceId));

		assertFalse(service1.isActive());
		assertEquals(0, manager.instanceList().size());
		assertFalse(manager.instanceList().contains(service1));
		assertFalse(manager.hasInstance(service1.instanceId()));

		switcher.enable(TidgetDependencyImpl.NAME);
		Thread.sleep(300);

		assertTrue("must have", manager.hasConfig(instanceId));

		final Tidget service2 = manager.instance(instanceId);

		assertFalse(service1 == service2);

		assertEquals(service1.instanceId(), service2.instanceId());

		assertTrue(service2.isActive());
		assertEquals(1, manager.instanceList().size());
		assertTrue(manager.instanceList().contains(service2));
		assertTrue(manager.hasInstance(service2.instanceId()));

		switcher.disable(TidgetDependencyImpl.NAME);
		Thread.sleep(300);

		assertTrue("must have", manager.hasConfig(instanceId));

		manager.configDestroy(instanceId);
		Thread.sleep(200);

		assertFalse("must not have", manager.hasConfig(instanceId));

		/** wait for logging pretty-print */
		Thread.sleep(100);

	}

}
