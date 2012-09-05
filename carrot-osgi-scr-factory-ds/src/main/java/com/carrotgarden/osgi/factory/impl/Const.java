/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

/** available SCR ComponentFactory service properties */
interface Const {

	String PROP_COMP_NAME = "component.name"; // String

	String PROP_COMP_FACTORY = "component.factory"; // String

	String PROP_SERVICE_ID = "service.id"; // Long

	String PROP_SERVICE_PID = "service.pid"; // String

	String PROP_SERVICE_VENDOR = "service.vendor"; // String

	String PROP_SERVICE_DESCRIPTION = "service.description"; // String

	String PROP_OBJECT_CLASS = "objectClass"; // String[]

}
