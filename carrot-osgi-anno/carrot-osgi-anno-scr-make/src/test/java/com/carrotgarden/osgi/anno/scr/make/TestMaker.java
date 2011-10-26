package com.carrotgarden.osgi.anno.scr.make;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMaker {

	static final Logger log = LoggerFactory.getLogger(TestMaker.class);

	@Test
	public void test0() {

		final Class<?> klaz = Comp1.class;

		final List<Class<?>> list = Maker.getClassList(klaz);

		for (final Class<?> k : list) {
			log.debug("k : {}", k);
		}

		assertTrue(true);

	}

	@Test
	public void test1() {

		assertFalse(Maker.isAnnotationPresent(Comp0.class));

		assertTrue(Maker.isAnnotationPresent(Comp1.class));

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
