/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;

import com.carrotgarden.osgi.factory.impl.CidgetManagerProvider;

/** base class for component factory manager implementations */
public abstract class CidgetManagerBase<C extends Cidget> extends
		CidgetManagerProvider<C> implements CidgetManager<C> {

	/** component interface used as component bind/unbind filter */
	@Override
	protected abstract Class<C> interfaceClass();

}
