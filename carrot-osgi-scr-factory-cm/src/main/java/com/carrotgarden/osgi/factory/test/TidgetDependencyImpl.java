/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component( //
name = TidgetDependencyImpl.NAME //
)
public class TidgetDependencyImpl implements TidgetDependency {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	public static final String NAME = "component-dependency";

	public static int countActivate;
	public static int countModified;
	public static int countDeactivate;

	public static Map<String, String> properties;

	@Activate
	protected void activate(final Map<String, String> props) throws Exception {

		Thread.sleep(100);

		countActivate++;

		log.info("@@@ activate : {}", props);

		properties = props;

	}

	@Modified
	protected void modified(final Map<String, String> props) throws Exception {

		Thread.sleep(100);

		countModified++;

		log.info("@@@ modified : {}", props);

		properties = props;

	}

	@Deactivate
	protected void deactivate(final Map<String, String> props) throws Exception {

		Thread.sleep(100);

		countDeactivate++;

		log.info("@@@ deactivate : {}", props);

		properties = props;

	}

}
