/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.bean.ProvideBean;
import com.thoughtworks.xstream.XStream;

public class MainXML {

	static final Logger log = LoggerFactory.getLogger(MainXML.class);

	public static void main(final String[] args) {

		log.debug("init");

		final XStream xstream = new XStream();

		xstream.autodetectAnnotations(true);

		// xstream.registerConverter(new PolicyConverter());
		// xstream.processAnnotations(ComponentBean.class);

		final ComponentBean bean = new ComponentBean();

		bean.service.provideList.add(new ProvideBean());

		log.debug("\n{}", xstream.toXML(bean));

	}
}
