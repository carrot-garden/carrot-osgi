package com.carrotgarden.osgi.anno.scr.make;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.AggregatorBean;
import com.carrotgarden.osgi.anno.scr.util.Util;
import com.thoughtworks.xstream.XStream;

public class Maker {

	private static final Logger log = LoggerFactory.getLogger(Maker.class);

	private final XStream xstream;

	private final Builder builder;

	public Maker() {

		this(new HashSet<String>());

	}

	public Maker(final Set<String> excludedServiceSet) {

		xstream = new XStream();
		xstream.autodetectAnnotations(true);

		builder = new Builder(excludedServiceSet);

	}

	/**
	 * generate SCR xml descriptors
	 * 
	 * @return valid xml or null
	 * 
	 */
	public String make(final Class<?>... klazArray) {

		final List<Class<?>> klazList = new LinkedList<Class<?>>();

		for (final Class<?> klaz : klazArray) {

			if (Util.isAbstract(klaz)) {
				/**
				 * abstract classes are processed as part of component
				 * inheritance
				 */
				continue;
			}

			if (!Util.hasComponentAnno(klaz)) {
				/**
				 * interested in @Component annotated only
				 */
				continue;
			}

			klazList.add(klaz);

		}

		if (klazList.isEmpty()) {
			return null;
		}

		final AggregatorBean bean = builder.makeAggregator(klazList);

		return xstream.toXML(bean);

	}

}
