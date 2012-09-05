/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import com.carrotgarden.osgi.factory.test.bench.CompManager;

//@Ignore
@RunWith(JUnit4TestRunner.class)
public class TestBench extends TestAny {

	@Inject
	private CompManager manager;

	@Test
	public void testBench() throws Exception {

		log.info("### hi");

		assertNotNull(manager);

		log.info("### bye");

	}

}
