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
public class TestFactoryCreate extends TestAny {

	@Inject
	protected TidgetManager manager;

	@Inject
	protected TidgetSwitch switcher;

	/** check create */
	@Test
	public void testFactory3() throws Exception {

		final String instanceId = "reader-001";

		{

			final Map<String, String> props = new HashMap<String, String>();

			final Tidget service = manager.instanceCreate(TidgetComp1.NAME, props);

			props.put(Cidget.PROP_INSTANCE_ID, null);

			assertNull("should not create w/o instance id", service);

		}

		{

			final Map<String, String> props = new HashMap<String, String>();

			props.put(Cidget.PROP_INSTANCE_ID, instanceId);

			final Tidget service = manager.instanceCreate(TidgetComp1.NAME, props);

			assertNotNull("should create just fine", service);

		}

		{

			final Map<String, String> props = new HashMap<String, String>();

			props.put(Cidget.PROP_INSTANCE_ID, instanceId);

			final Tidget service = manager.instanceCreate(TidgetComp1.NAME, props);

			assertNull("should not create a duplicate", service);

		}

	}

}
