package com.carrotgarden.osgi.anno.scr.make;

import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.thoughtworks.xstream.XStream;

public class Maker {

	static final Logger log = LoggerFactory.getLogger(Maker.class);

	private final XStream xstream;

	public Maker() {

		xstream = new XStream();

		xstream.autodetectAnnotations(true);

	}

	public static boolean isAnnotationPresent(final Class<?> klaz) {

		if (klaz == null) {
			return false;
		}

		if (klaz.isAnnotationPresent(Component.class)) {
			return true;
		} else {
			return isAnnotationPresent(klaz.getSuperclass());
		}

	}

	public static List<Class<?>> getClassList(final Class<?> klaz) {

		final List<Class<?>> list = new LinkedList<Class<?>>();

		Class<?> type = klaz;

		while (true) {

			if (type == null) {
				return list;
			}

			list.add(0, type);

			type = type.getSuperclass();

		}

	}

	public static ComponentBean makeBean(final Class<?> klaz) {

		final ComponentBean bean = new ComponentBean();

		final List<Class<?>> list = getClassList(klaz);

		for (final Class<?> type : list) {

			bean.apply(type);

		}

		return bean;

	}

	public String make(final Class<?> klaz) {

		if (isAnnotationPresent(klaz)) {

			final ComponentBean bean = makeBean(klaz);

			return xstream.toXML(bean);

		}

		return null;

	}

}
