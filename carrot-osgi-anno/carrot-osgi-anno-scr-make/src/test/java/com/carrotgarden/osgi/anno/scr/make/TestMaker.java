package com.carrotgarden.osgi.anno.scr.make;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMaker {

	static final Logger log = LoggerFactory.getLogger(TestMaker.class);

	@Test
	public void test0() {

		assertTrue(true);

	}

	@Test
	public void test1() {

	}

	@Test
	public void test2() {

		final Maker maker = new Maker();

		log.debug("bean : \n{}", maker.make(Comp1.class));

	}

	// @Test
	public void test3() {

		final Method[] methodArray = Comp1.class.getDeclaredMethods();

		for (final Method method : methodArray) {

			log.debug("method : {}", method);

			if (method.isAnnotationPresent(Reference.class)) {
				log.debug("isAnnotationPresent : {}", method);
			}

		}

	}

}
