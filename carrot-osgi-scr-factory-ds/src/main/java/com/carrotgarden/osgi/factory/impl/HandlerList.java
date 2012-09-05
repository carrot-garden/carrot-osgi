/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.concurrent.CopyOnWriteArrayList;

import com.carrotgarden.osgi.factory.api.FactoryHandler;
import com.carrotgarden.osgi.factory.api.FidgetManager;

@SuppressWarnings("serial")
class HandlerList<M extends FidgetManager<?>> extends
		CopyOnWriteArrayList<FactoryHandler<M>> {

}
