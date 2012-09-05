/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.event.api;

import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.carrotgarden.osgi.event.impl.ClassUtil;
import com.carrotgarden.osgi.event.impl.ClassUtil.ClassTrace;

public final class EventUtil {

	/** make event class path event osgi complaint */
	public static String filterPath(final String path) {
		return path.replace('.', '/').replace('$', '/');
	}

	/** make event action event osgi complaint */
	public static String filterAction(final String action) {
		return action.replace('/', '_').replace('$', '-');
	}

	/**
	 * event name per osgi spec v 4.3 $ 113.3.1, based on provided class name
	 * and action
	 */
	public static String name(final Class<?> klaz, final String action) {
		final String name = klaz.getName();
		final String specPath = filterPath(name);
		final String specAction = filterPath(action);
		final String eventName = specPath + "/" + specAction;
		return eventName;
	}

	/**
	 * event name per osgi spec v 4.3 $ 113.3.1, based on caller class name and
	 * action
	 */
	public static String name(final String action) {
		final ClassTrace trace = new ClassUtil.ClassTrace();
		final Class<?> klaz = trace.getClassAt(3);
		return name(klaz, action);
	}

	/** non blocking */
	public static void post(final EventAdmin eventAdmin, final String eventName) {
		post(eventAdmin, eventName, null);
	}

	/** blocking */
	public static void send(final EventAdmin eventAdmin, final String eventName) {
		send(eventAdmin, eventName, null);
	}

	/** non blocking */
	public static void post(final EventAdmin eventAdmin,
			final String eventName, final Map<String, String> eventProps) {
		eventAdmin.postEvent(new Event(eventName, eventProps));
	}

	/** blocking */
	public static void send(final EventAdmin eventAdmin,
			final String eventName, final Map<String, String> eventProps) {
		eventAdmin.sendEvent(new Event(eventName, eventProps));
	}

	/** is event topic among these names */
	public static boolean is(final Event event, final String... nameArray) {
		if (event == null || nameArray == null) {
			return false;
		}
		final String topic = event.getTopic();
		for (final String name : nameArray) {
			if (topic.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/** extract string property form event */
	public static String getProperty(final Event event, final String name) {
		return (String) event.getProperty(name);
	}

	private EventUtil() {
	}

}
