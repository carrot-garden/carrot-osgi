package com.carrotgarden.osgi.anno.scr.make;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.thoughtworks.xstream.XStream;

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

		final XStream xstream = new XStream();

		xstream.autodetectAnnotations(true);

		final ComponentBean bean = Maker.makeBean(Comp1.class);

		log.debug("bean : \n{}", xstream.toXML(bean));

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
