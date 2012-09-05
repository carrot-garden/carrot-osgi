/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.event;

import static org.junit.Assert.*;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import com.carrotgarden.osgi.event.api.EventAdminService;
import com.carrotgarden.osgi.event.api.EventUtil;

@RunWith(JUnit4TestRunner.class)
public class TestEventAdmin extends TestAny implements EventHandler {

	private final String TOPIC = UUID.randomUUID().toString();

	@Inject
	private EventAdminService eventService;

	@SuppressWarnings("rawtypes")
	@Test
	public void testEventService() throws Exception {

		assertNotNull(eventService);

		final Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(EventConstants.EVENT_TOPIC, TOPIC);

		final ServiceRegistration reg = context().registerService(
				EventHandler.class.getName(), this, props);

		eventService.send(TOPIC);

		assertEquals(eventCount, 1);

		reg.unregister();

		log.info("################################");

	}

	private int eventCount;

	@Override
	public void handleEvent(final Event event) {

		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("event topic : {}", event.getTopic());
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		assertTrue(EventUtil.is(event, TOPIC));

		eventCount++;

	}

}
