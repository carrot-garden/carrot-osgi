/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;

import java.util.List;
import java.util.Map;

import org.osgi.service.event.Event;

/**
 * Declarative Services Factory Component manager;
 * 
 * derive your factory management interfaces from this
 */
public interface FidgetManager<F extends Fidget> {

	/**
	 * @return factory properties map, never null, will be empty for missing
	 *         descriptor
	 */
	Map<String, String> getFactoryDescriptor(String factoryId);

	//

	/**
	 * @return {@link FactoryEvent#PROP_FACTORY_ID} or null if event is not
	 *         fired by this manager or this is a non factory event
	 */
	String getFactoryId(Event event);

	/**
	 * @return true if manager posted {@link FactoryEvent#FACTORY_ENABLED}
	 */
	boolean hasFiredEnabled(Event event);

	/**
	 * @return true if manager posted {@link FactoryEvent#FACTORY_DISABLED}
	 */
	boolean hasFiredDisabled(Event event);

	//

	/** @return true if factory id enabled */
	boolean hasFactory(String factoryId);

	/** @return list of enabled factory id */
	List<String> getFactoryIds();

	//

	/** @return is this fidget live? */
	boolean hasInstance(String instanceId);

	/** @return live fidget or null */
	F getInstance(String instanceId);

	/** @return list of all live fidgets */
	List<F> getInstances();

	/** @return list of live fidgets for this factory id */
	List<F> getInstances(String factoryId);

	//

	/** @return live fidget or null for invalid conditions */
	F create(String factoryId, Map<String, String> fidgetProps);

	/** @return true if fidget destroy succeeded */
	boolean destroy(F fidget);

}
