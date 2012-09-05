/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test;

import org.osgi.service.component.annotations.Component;

import com.carrotgarden.osgi.factory.api.CidgetManagerBase;

@Component
public class TidgetManagerProvider extends CidgetManagerBase<Tidget> implements
		TidgetManager {

	@Override
	protected Class<Tidget> interfaceClass() {
		return Tidget.class;
	}

}
