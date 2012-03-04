/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.make;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMaker2 {

	static final Logger log = LoggerFactory.getLogger(TestMaker2.class);

	static String convertStreamToString(final InputStream input) {

		if (input == null) {
			return null;
		}

		try {
			return new java.util.Scanner(input).useDelimiter("\\A").next();
		} catch (final Exception e) {
			return "";
		}
	}

	static void testClass(final String name) throws Throwable {

		final ClassLoader loader = TestMaker2.class.getClassLoader();

		log.debug("######################################");
		log.debug("test class : {}", name);

		final Maker maker = new Maker();

		final String source = maker.make(loader, name);
		log.debug("source : \n{}", source);

		final Class<?> klaz = Class.forName(name);

		final String fileName = klaz.getSimpleName() + ".xml";
		final InputStream input = klaz.getResourceAsStream(fileName);
		final String target = convertStreamToString(input);
		log.debug("target : \n{}", target);

		assertEquals(source, target);

	}

	@Test
	public void test01() throws Throwable {

		final String pack = "com.carrotgarden.osgi.anno.scr.case01.";

		testClass(pack + "Comp_01_empty");

		testClass(pack + "Comp_01_with_name");

		testClass(pack + "Comp_01_service");

		testClass(pack + "Comp_01_reference");

		testClass(pack + "Comp_01_reference_dynamic");

		testClass(pack + "Comp_01_factory");

	}

	@Test
	public void test02() throws Throwable {

		final String pack = "com.carrotgarden.osgi.anno.scr.case02.";

		testClass(pack + "Comp_02_0"); // missing

		testClass(pack + "Comp_02_1");

		testClass(pack + "Comp_02_2");

		testClass(pack + "Comp_02_3"); // missing

	}

}
