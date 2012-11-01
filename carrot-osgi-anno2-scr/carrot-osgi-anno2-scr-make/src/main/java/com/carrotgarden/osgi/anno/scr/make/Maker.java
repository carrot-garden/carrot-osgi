/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.make;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.AggregatorBean;
import com.thoughtworks.xstream.XStream;

public class Maker {

	static final Logger log = LoggerFactory.getLogger(Maker.class);

	private final XStream xstream;

	static final String NAME_SPACE = "http://www.osgi.org/xmlns/scr/v1.1.0";
	static final String NAME_PREFIX = "scr";

	public Maker() {

		// final QNameMap nameMap = new QNameMap();
		// nameMap.setDefaultNamespace(NAME_SPACE);
		// nameMap.setDefaultPrefix(NAME_PREFIX);
		// final StaxDriver driver = new StaxDriver(nameMap);
		// xstream = new XStream(driver);

		xstream = new XStream();
		xstream.autodetectAnnotations(true);

	}

	public String make(final Class<?>... klazArray) {

		final AggregatorBean bean = new AggregatorBean();

		bean.apply(klazArray);

		if (bean.componentList.size() == 0) {
			return null;
		}

		return xstream.toXML(bean);

	}

}
