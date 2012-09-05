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
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component( //
name = TidgetComp2.NAME, //
configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class TidgetComp2 implements Tidget {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	public static final String NAME = "component-two";

	private String id;

	@Override
	public String instanceId() {
		return id;
	}

	@Override
	public String getSomeName() {
		return null;
	}

	public static int countActivate;
	public static int countModified;
	public static int countDeactivate;

	public static Map<String, String> properties;

	@Activate
	protected void activate(final Map<String, String> props) throws Exception {

		id = props.get(PROP_INSTANCE_ID);

		Thread.sleep(100);

		countActivate++;

		log.info("@@@ activate : {}", props);

		properties = props;

		isActive = true;

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

		isActive = false;

	}

	@Reference
	protected void bind(final TidgetDependency s) {

	}

	protected void unbind(final TidgetDependency s) {

	}

	@Override
	public String toString() {
		return "tidget1 : " + instanceId();
	}

	private boolean isActive;

	@Override
	public boolean isActive() {
		return isActive;
	}

}
