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
import com.carrotgarden.osgi.factory.test.TidgetManager;
import com.carrotgarden.osgi.factory.test.TidgetSwitch;

@RunWith(JUnit4TestRunner.class)
public class TestFactoryCycle extends TestAny {

	@Inject
	protected TidgetManager manager;

	@Inject
	protected TidgetSwitch switcher;

	/** verify life cycle */
	@Test
	public void testFactory1() throws Exception {

		final String instance1 = "reader-001";

		assertEquals(0, TidgetComp1.countActivate);
		assertEquals(0, TidgetComp1.countModified);
		assertEquals(0, TidgetComp1.countDeactivate);

		final Map<String, String> props = new HashMap<String, String>();
		props.put(Cidget.PROP_INSTANCE_ID, instance1);

		// ########################

		props.put("hello", "step-create");

		final Tidget service1 = manager.instanceCreate(TidgetComp1.NAME, props);

		assertNotNull("got service 1", service1);

		assertEquals(1, TidgetComp1.countActivate);
		assertEquals(0, TidgetComp1.countModified);
		assertEquals(0, TidgetComp1.countDeactivate);

		assertEquals("step-create", TidgetComp1.properties.get("hello"));

		final String instanceId = service1.instanceId();

		final Tidget service2 = manager.instance(instanceId);

		assertNotNull("got service 2", service2);

		assertTrue("same service", service1 == service2);

		assertTrue("must have", manager.hasConfig(instance1));

		// ########################

		props.put("hello", "step-update-1");

		assertTrue("update ok", manager.instanceUpdate(instanceId, props));

		assertEquals(1, TidgetComp1.countActivate);
		assertEquals(1, TidgetComp1.countModified);
		assertEquals(0, TidgetComp1.countDeactivate);

		assertEquals("step-update-1", TidgetComp1.properties.get("hello"));

		// ########################

		props.put("hello", "step-update-2");

		assertTrue("update ok", manager.instanceUpdate(instanceId, props));

		assertEquals(1, TidgetComp1.countActivate);
		assertEquals(2, TidgetComp1.countModified);
		assertEquals(0, TidgetComp1.countDeactivate);

		assertEquals("step-update-2", TidgetComp1.properties.get("hello"));

		// ########################

		assertTrue("destroy ok", manager.instanceDestroy(instanceId));

		/** the @Deactivate completes asynchronously */
		Thread.sleep(200);

		assertEquals(1, TidgetComp1.countActivate);
		assertEquals(2, TidgetComp1.countModified);
		assertEquals(1, TidgetComp1.countDeactivate);

		assertNull(TidgetComp1.properties.get("hello"));

		assertFalse("must be gone", manager.hasConfig(instance1));

		/** wait for logging pretty-print */
		Thread.sleep(100);

	}

}
