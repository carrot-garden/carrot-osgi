/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;


/** config admin driven component factory widget */
public interface Cidget {

	/* props */

	/** optional #@Activate #@Deactivate #@Modified operation timeout */
	String PROP_TIMEOUT = System.getProperty(Cidget.class.getName()
			+ ".timeout.property", "operation-timeout");

	/** default operation timeout value, milliseconds */
	String DEFAULT_TIMEOUT = System.getProperty(Cidget.class.getName()
			+ ".timeout.default-value", "3000");

	/** mandatory factory id property name */
	String PROP_FACTORY_ID = System.getProperty(Cidget.class.getName()
			+ ".factory.id", "factory.id");

	/**
	 * mandatory external UUID of the instance provided via properties at create
	 * or update
	 */
	String PROP_INSTANCE_ID = System.getProperty(Cidget.class.getName()
			+ ".instance.id", "instance.id");

	/* methods */

	/** UUID of this instance */
	String instanceId();

}
