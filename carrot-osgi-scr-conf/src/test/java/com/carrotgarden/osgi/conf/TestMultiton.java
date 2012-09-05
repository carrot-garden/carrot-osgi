/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf;

import static org.junit.Assert.*;

import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Constants;

import com.carrotgarden.osgi.conf.api.ConfigAdminService;
import com.carrotgarden.osgi.conf.test.TestCompMultiple;

@RunWith(JUnit4TestRunner.class)
public class TestMultiton extends TestAny {

	@Inject
	protected ConfigAdminService configAdmin;

	@Test
	public void testMultiton() throws Exception {

		final String factoryId = TestCompMultiple.NAME;

		assertEquals("start with no factory component instances", 0,
				TestCompMultiple.count.get());

		assertTrue("start with no persistent configuration for the factory",
				configAdmin.listMultiton(factoryId).isEmpty());

		final String pid = configAdmin.multitonCreate(factoryId);
		Thread.sleep(200);

		assertTrue("should have persistent component configuration now",
				configAdmin.isPresent(pid));

		assertEquals("should have live component instance now", 1,
				TestCompMultiple.count.get());

		assertEquals("should have same count of components and configurations",
				configAdmin.listMultiton(factoryId).size(),
				TestCompMultiple.count.get());

		final Map<String, String> props = configAdmin.properties(pid);

		assertFalse("persistent configuration can not be empty",
				props.isEmpty());

		assertNotNull("persistent configuration must have a PID",
				props.get(Constants.SERVICE_PID));

		props.put("test-key", "test-value-0");

		assertTrue(configAdmin.multitonUpdate(pid, props));
		Thread.sleep(200);

		assertEquals("still have active components after update", 1,
				TestCompMultiple.count.get());

		props.put("test-key", "test-value-1");

		assertTrue(configAdmin.multitonUpdate(pid, props));
		Thread.sleep(200);

		assertTrue(configAdmin.multitonDestroy(pid));
		Thread.sleep(200);

		assertEquals("now the scr factory component is terminated", 0,
				TestCompMultiple.count.get());

		assertFalse("and component persistent configuration is also gone",
				configAdmin.isPresent(pid));

	}

}
