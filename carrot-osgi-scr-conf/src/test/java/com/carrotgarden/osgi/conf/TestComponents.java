/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import com.carrotgarden.osgi.conf.api.ConfigAdminService;

@RunWith(JUnit4TestRunner.class)
public class TestComponents extends TestAny {

	@Inject
	protected ConfigAdminService configAdmin;

	/** non-existing component config */
	@Test
	public void test0() throws Exception {

		final String pid = UUID.randomUUID().toString();

		assertFalse("config is persisted", configAdmin.isPresent(pid));

		assertTrue("config create ok", configAdmin.singletonCreate(pid, null));

		assertTrue("config is persisted", configAdmin.isPresent(pid));

		assertTrue("config is destroyed", configAdmin.singletonDestroy(pid));

		assertFalse("config is persisted", configAdmin.isPresent(pid));

	}

	@Test
	public void test1() throws Exception {

	}

}
