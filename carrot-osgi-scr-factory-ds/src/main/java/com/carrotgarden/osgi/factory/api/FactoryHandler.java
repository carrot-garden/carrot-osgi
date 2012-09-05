/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;

/**
 * provide this service from your factory controller to receive factory life
 * cycle event notifications
 */
public interface FactoryHandler<M extends FidgetManager<?>> {

	boolean ON = true;
	boolean OFF = false;

	//

	/** @return {@link FidgetManager} bound into your factory controller */
	M getBoundManager();

	//

	/** life cycle event notification method; called from pool thread */
	void handleFactory(String factoryId, boolean isOn);

}
