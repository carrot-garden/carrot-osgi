/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;

import java.net.URL;
import java.util.Properties;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAny {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private static final String PAX_LEVEL = "ERROR";

	@Configuration
	public Option[] config() {

		log.debug("### config");

		return options(

				systemTimeout(3 * 1000),

				systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
						.value(PAX_LEVEL),

				junitBundles(),

				mavenBundle().groupId("org.apache.felix")
						.artifactId("org.apache.felix.configadmin")
						.versionAsInProject(),
				mavenBundle().groupId("org.apache.felix")
						.artifactId("org.apache.felix.eventadmin")
						.versionAsInProject(),
				mavenBundle().groupId("org.apache.felix")
						.artifactId("org.apache.felix.scr")
						.versionAsInProject(),

				mavenBundle().groupId("com.carrotgarden.osgi")
						.artifactId("carrot-osgi-anno-scr-core")
						.versionAsInProject(),

				//

				mavenBundle().groupId("org.ops4j.pax.logging").artifactId(
						"pax-logging-api"),
				mavenBundle().groupId("org.ops4j.pax.logging").artifactId(
						"pax-logging-service"),

				//
				mavenBundle().groupId("com.carrotgarden.osgi")
						.artifactId("carrot-osgi-scr-event")
						.versionAsInProject(),

				mavenBundle().groupId("org.apache.sling")
						.artifactId("org.apache.sling.commons.threads")
						.versionAsInProject(),

				//

				bundle("reference:file:target/classes")

		);

	}

	@Inject
	private BundleContext context;

	protected BundleContext context() {
		return context;
	}

	@Inject
	private ConfigurationAdmin configAdmin;

	@Before
	public void testActivate() throws Exception {

		assertNotNull(context);
		assertNotNull(configAdmin);

		loggingActivate(configAdmin);

		log.info("#########################################");
		log.info("###              ACTIVATE             ###");

		for (final Bundle bundle : context.getBundles()) {
			// log.info("### bundle : {}", bundle.getSymbolicName());
		}

	}

	@After
	public void testDeactivate() throws Exception {

		log.info("###             DEACTIVATE            ###");
		log.info("#########################################");

		loggingDeactivate(configAdmin);

	}

	private static final String PAX_PID = "org.ops4j.pax.logging";
	private static final String PAX_SERVICE = "org.ops4j.pax.logging.PaxLoggingService";

	private void loggingActivate(final ConfigurationAdmin configAdmin)
			throws Exception {

		final URL propsURL = TestAny.class.getResource("/log4j.properties");

		final Properties props = new Properties();

		props.load(propsURL.openStream());

		final org.osgi.service.cm.Configuration config = configAdmin
				.getConfiguration(PAX_PID, null);

		config.update(props);

		final ServiceTracker tracker = new ServiceTracker(context, PAX_SERVICE,
				null);

		tracker.open(true);

		final Object service = tracker.waitForService(3 * 1000);

		assertNotNull(service);

		Thread.sleep(500);

	}

	private void loggingDeactivate(final ConfigurationAdmin configAdmin)
			throws Exception {

		final org.osgi.service.cm.Configuration config = configAdmin
				.getConfiguration(PAX_PID, null);

		config.delete();

		Thread.sleep(500);

	}

}
