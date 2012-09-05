/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;

import com.carrotgarden.osgi.event.api.EventUtil;

/**
 * life cycle events & props
 */
public interface FactoryEvent {

	/* events */

	String ALL = EventUtil.name("*");

	String FACTORY_ENABLED = EventUtil.name("FACTORY_ENABLED");
	String FACTORY_DISABLED = EventUtil.name("FACTORY_DISABLED");

	/* props */

	String PROP_FACTORY_ID = "factory_id";

}
