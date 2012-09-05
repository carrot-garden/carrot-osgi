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

/** config admin component factory manager */
public interface CidgetManager<C extends Cidget> {

	/**
	 * blocking call with timeout;
	 * 
	 * create new or update existing instance
	 * 
	 * @return live instance or null on failure
	 */
	C instanceEnsure(String factoryId, Map<String, String> properties);

	/**
	 * blocking call with timeout;
	 * 
	 * @return live instance or null on #@Activate failure
	 */
	C instanceCreate(String factoryId, Map<String, String> properties);

	/**
	 * blocking call with timeout;
	 * 
	 * @return was #@Modified a success?
	 */
	boolean instanceUpdate(String instanceId, Map<String, String> properties);

	/**
	 * blocking call with timeout;
	 * 
	 * @return was #@Deactivate a success?
	 */
	boolean instanceDestroy(String instanceId);

	/** @return existing live instance or null if not present */
	C instance(String instanceId);

	/** @return current list of live factory components */
	List<C> instanceList();

	/** @return is live instance present? */
	boolean hasInstance(String instanceId);

	//

	/**
	 * @return is persistent instance configuration present; regardless if live
	 *         instance exists
	 */
	boolean hasConfig(String instanceId);

	/**
	 * create new or update existing persistent configuration w/o waiting for
	 * instance
	 */
	boolean configEnsure(String factoryId, Map<String, String> properties);

	/** create persistent configuration w/o waiting for instance */
	boolean configCreate(String factoryId, Map<String, String> properties);

	/** update persistent configuration w/o waiting for instance */
	boolean configUpdate(String instanceId, Map<String, String> properties);

	/** destroy persistent configuration w/o waiting for instance */
	boolean configDestroy(String instanceId);

	/**
	 * @return persistent configuration for live or dead instance; empty if
	 *         missing
	 */
	Map<String, String> config(String instanceId);

	/** @return currently persisted configurations made my this factory */
	List<Map<String, String>> configList();

}
