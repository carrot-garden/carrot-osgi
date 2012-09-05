/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import com.carrotgarden.osgi.factory.api.FidgetManager;

/** internal */
public interface HandlerSender<M extends FidgetManager<?>> {

	void sendState(M manager, String factoryId, boolean isOn);

}
