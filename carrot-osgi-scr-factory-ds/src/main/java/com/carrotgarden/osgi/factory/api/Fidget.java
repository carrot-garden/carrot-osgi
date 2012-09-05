/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;

/**
 * Declarative Services Factory Component interface;
 * 
 * derive your factory component interfaces from this
 */
public interface Fidget {

	/** @return immutable UUID of this component instance */
	String getInstanceId();

}
