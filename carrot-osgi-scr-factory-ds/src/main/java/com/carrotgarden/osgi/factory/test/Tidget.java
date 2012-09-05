/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test;

import java.util.Map;

import com.carrotgarden.osgi.factory.api.Fidget;

/** testing widget */
public interface Tidget extends Fidget {

	Map<String, String> getProps();

}
