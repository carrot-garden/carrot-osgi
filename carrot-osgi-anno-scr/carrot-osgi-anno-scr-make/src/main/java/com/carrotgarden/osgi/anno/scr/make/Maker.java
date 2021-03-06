/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
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

	@SuppressWarnings("unused")
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
	 * 
	 * generate SCR xml descriptors; classes must be initialized
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

	static final boolean INIT_NOT = false;
	static final boolean INIT_YES = true;

	/**
	 * 
	 * generate SCR xml descriptors; classes will be loaded and initialized as
	 * needed
	 * 
	 * @return valid xml or null
	 * 
	 */
	public String make(final ClassLoader loader, final String... nameArray)
			throws Throwable {

		final List<Class<?>> klazList = new LinkedList<Class<?>>();

		for (final String name : nameArray) {

			final Class<?> klaz = Class.forName(name, INIT_NOT, loader);

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

			/** force class hierarchy initialization */
			Class.forName(name, INIT_YES, loader);

			klazList.add(klaz);

		}

		if (klazList.isEmpty()) {
			return null;
		}

		final AggregatorBean bean = builder.makeAggregator(klazList);

		return xstream.toXML(bean);

	}

}
