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

import com.carrotgarden.osgi.conf.api.ConfigAdminService;
import com.carrotgarden.osgi.conf.test.TestCompSingle1;
import com.carrotgarden.osgi.conf.test.TestCompSingle2;
import com.carrotgarden.osgi.conf.test.TestSwitcher;

@RunWith(JUnit4TestRunner.class)
public class TestSingleton extends TestAny {

	@Inject
	protected ConfigAdminService configAdmin;

	@Inject
	protected TestSwitcher switcher;

	/** component with configurationPolicy = ConfigurationPolicy.OPTIONAL */
	@Test
	public void testSingleton1() throws Exception {

		final String pid = TestCompSingle1.NAME;

		assertEquals("start with component disabled", 0,
				TestCompSingle1.count.get());

		assertFalse("start with no persistent config",
				configAdmin.isPresent(pid));

		switcher.enable(TestCompSingle1.NAME);
		Thread.sleep(200);

		assertEquals("component is now activated", 1,
				TestCompSingle1.count.get());

		assertFalse("still have no config", configAdmin.isPresent(pid));

		final Map<String, String> props = new HashMap<String, String>();
		props.put("test-key", "test-value");

		assertTrue(configAdmin.singletonUpdate(TestCompSingle1.NAME, props));
		Thread.sleep(200);

		assertTrue("has config now", configAdmin.isPresent(pid));

		assertEquals("component is still activated", 1,
				TestCompSingle1.count.get());

		assertTrue(configAdmin.singletonRemove(TestCompSingle1.NAME));
		Thread.sleep(200);

		assertFalse("config is gone", configAdmin.isPresent(pid));

		switcher.disable(TestCompSingle1.NAME);
		Thread.sleep(200);

		assertEquals("finish with component disabled", 0,
				TestCompSingle1.count.get());

	}

	/** component with configurationPolicy = ConfigurationPolicy.REQUIRE */
	@Test
	public void testSingleton2() throws Exception {

		final String pid = TestCompSingle2.NAME;

		final Map<String, String> props = configAdmin.properties(pid);

		assertTrue("missing persistent configuration brings empty props",
				props.isEmpty());

		assertEquals("start with component disabled", 0,
				TestCompSingle2.count.get());

		assertTrue(configAdmin.singletonCreate(pid, null));
		Thread.sleep(200);

		assertEquals("now component is active", 1, TestCompSingle2.count.get());

		assertTrue("component has persistent configuration",
				configAdmin.isPresent(pid));

		props.put("test-key", "test-value");

		assertTrue(configAdmin.singletonUpdate(pid, props));
		Thread.sleep(200);

		assertTrue("should have persistent configuration",
				configAdmin.isPresent(pid));

		assertTrue(configAdmin.singletonDestroy(pid));
		Thread.sleep(200);

		assertFalse("persistent configuration is gone",
				configAdmin.isPresent(pid));

		assertEquals("component is gone", 0, TestCompSingle2.count.get());

		assertEquals("now component is disabled", 0,
				TestCompSingle2.count.get());

	}

}
