/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
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
import com.carrotgarden.osgi.anno.scr.util.UtilAsm;
import com.carrotgarden.osgi.anno.scr.util.UtilJdk;
import com.thoughtworks.xstream.XStream;

/**
 * Entry point into DS component descriptor maker API.
 * <p>
 * Uses DS annotations throughout class inheritance.
 */
public class Maker {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(Maker.class);

	private final XStream xstream;

	private final Builder builder;

	/**
	 * Maker w/o any excluded services.
	 */
	public Maker() {

		this(new HashSet<String>());

	}

	/**
	 * Maker with provided excluded services.
	 */
	public Maker(final Set<String> excludedServiceSet) {

		xstream = new XStream();
		xstream.autodetectAnnotations(true);

		builder = new Builder(excludedServiceSet);

	}

	/**
	 * Generate DS XML descriptors. Classes must be already initialized.
	 * 
	 * @return valid DS XML or null if there are no components
	 */
	public String make(final Class<?>... klazArray) throws Throwable {

		final List<Class<?>> klazList = new LinkedList<Class<?>>();

		for (final Class<?> klaz : klazArray) {

			if (UtilJdk.isAbstract(klaz)) {
				/**
				 * Abstract classes are processed as part of component
				 * inheritance.
				 */
				continue;
			}

			if (!UtilAsm.hasComponentAnno(klaz)) {
				/**
				 * Interested in @Component annotated only.
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
	 * Generate DS XML descriptors. Classes will be loaded and initialized as
	 * needed.
	 * 
	 * @return valid DS XML or null if there are no components.
	 */
	public String make(final ClassLoader loader, final String... nameArray)
			throws Throwable {

		final List<Class<?>> klazList = new LinkedList<Class<?>>();

		for (final String name : nameArray) {

			final Class<?> klaz = Class.forName(name, INIT_NOT, loader);

			if (UtilJdk.isAbstract(klaz)) {
				/**
				 * Abstract classes are processed as part of component
				 * inheritance.
				 */
				continue;
			}

			if (!UtilAsm.hasComponentAnno(klaz)) {
				/**
				 * Interested in @Component annotated only.
				 */
				continue;
			}

			/** Force class hierarchy initialization. */
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
