/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.event.impl;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;

import com.carrotgarden.osgi.event.api.EventAdminService;
import com.carrotgarden.osgi.event.api.EventUtil;

@Component
public class EventAdminServiceProvider implements EventAdminService {

	@Override
	public void post(final String topic) {
		EventUtil.post(eventAdmin, topic);
	}

	@Override
	public void send(final String topic) {
		EventUtil.send(eventAdmin, topic);
	}

	@Override
	public void post(final String topic, final Map<String, String> props) {
		EventUtil.post(eventAdmin, topic, props);
	}

	@Override
	public void send(final String topic, final Map<String, String> props) {
		EventUtil.send(eventAdmin, topic, props);
	}

	//

	private EventAdmin eventAdmin;

	@Reference
	protected void bind(final EventAdmin s) {
		eventAdmin = s;
	}

	protected void unbind(final EventAdmin s) {
		eventAdmin = null;
	}

}
