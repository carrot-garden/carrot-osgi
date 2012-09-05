/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.event.api;

import java.util.Map;

/**
 * convenience wrapper for osgi event admin service
 * 
 * http://felix.apache.org/site/apache-felix-event-admin.html
 */
public interface EventAdminService {

	/** non blocking */
	void post(String topic);

	/** blocking */
	void send(String topic);

	//

	/** non blocking */
	void post(String topic, Map<String, String> props);

	/** blocking */
	void send(String topic, Map<String, String> props);

}
