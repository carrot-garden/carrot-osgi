package com.carrotgarden.osgi.anno.scr.make;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;

public class Maker {

	static final Logger log = LoggerFactory.getLogger(Maker.class);

	private final XStream xstream;

	static final String NAME_SPACE = "http://www.osgi.org/xmlns/scr/v1.1.0";

	static final String NAME_PREFIX = "scr";

	public Maker() {

		final QNameMap nameMap = new QNameMap();
		nameMap.setDefaultNamespace(NAME_SPACE);
		nameMap.setDefaultPrefix(NAME_PREFIX);

		final StaxDriver driver = new StaxDriver(nameMap);

		xstream = new XStream(driver);
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
