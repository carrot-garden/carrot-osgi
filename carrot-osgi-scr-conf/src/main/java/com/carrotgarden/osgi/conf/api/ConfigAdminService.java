/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf.api;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.ConfigurationPolicy;

/**
 * convenience wrapper for osgi configuration admin service; adapted for
 * declarative services / scr metaphor;
 * 
 * see <a href=
 * "http://www.osgi.org/javadoc/r4v42/org/osgi/service/cm/ConfigurationAdmin.html"
 * >config admin spec</a>
 * 
 * singleton : means non - factory component;
 * 
 * multiton : means component to be made by component factory;
 * 
 * scr.activate() / scr.modified() / scr.deactivate() will run only when
 * scr.component dependencies are satisfied;
 * 
 * note: {@link #multitonCreate(String)} / {@link #Change(String)} /
 * {@link #singletonRemove(String)} make changes to the persistent configuration
 * store of the osgi configuration admin service bundle;
 * 
 * see tests for details; all invocations are asynchronous;
 */
public interface ConfigAdminService {

	/** invalid PID, can be returned from {@link #multitonCreate(String)} */
	String INVALID_PID = "";

	//

	/**
	 * properties currently persisted for this component;
	 * 
	 * note: only string properties are returned
	 */
	Map<String, String> properties(String servicePid);

	/**
	 * properties currently persisted for the components matching a filter;
	 * 
	 * note: only string properties are returned
	 */
	List<Map<String, String>> propertiesList(String filter);

	/**
	 * @return true, if this component persistent configuration exits
	 */
	boolean isPresent(String servicePid);

	/** not {@link #INVALID_PID} */
	boolean isValid(String servicePid);

	/** PID list of all components with persistent configurations */
	List<String> listAll();

	/** PID list of components matching a filter */
	List<String> listComponent(String filter);

	/** PID list of multiton components made by this factory */
	List<String> listMultiton(String factoryId);

	/** PID list of singleton components with persistent configurations */
	List<String> listSingleton();

	/**
	 * use with {@link ConfigurationPolicy.REQUIRE} components
	 * 
	 * make new component instance; calls src.activate() with provided
	 * properties;
	 * 
	 * @param factoryId
	 *            - component.name
	 * 
	 * @return new servicePid = factoryId.{random guid} OR {@link #INVALID_PID}
	 *         for error conditions
	 */
	String multitonCreate(String factoryId, Map<String, String> properties);

	/**
	 * use with {@link ConfigurationPolicy.REQUIRE} components;
	 * 
	 * destroy both configuration and the component instance
	 * 
	 * calls scr.deactivate(); then remove persistent configuration; the
	 * component is gone after this;
	 * 
	 * @param servicePid
	 *            - result of {@link #multitonCreate(String, Map)}
	 */
	boolean multitonDestroy(String servicePid);

	/**
	 * use with {@link ConfigurationPolicy.REQUIRE} components;
	 * 
	 * change persisted configuration for a multiton
	 * 
	 * calls scr.modified() with provided properties;
	 * 
	 * @param servicePid
	 *            - result of {@link #multitonCreate(String, Map)}
	 */
	boolean multitonUpdate(String servicePid, Map<String, String> properties);

	/**
	 * use with {@link ConfigurationPolicy.REQUIRE} components;
	 * 
	 * create persistent configuration; calls src.activate();
	 * 
	 * @param servicePid
	 *            - {@link #ComponentConstants.COMPONENT_NAME}
	 */
	boolean singletonCreate(String servicePid, Map<String, String> properties);

	/**
	 * use with {@link ConfigurationPolicy.REQUIRE} components;
	 * 
	 * calls src.deactivate(); remove persistent configuration;
	 * 
	 * @param servicePid
	 *            - {@link #ComponentConstants.COMPONENT_NAME}
	 */
	boolean singletonDestroy(String servicePid);

	/**
	 * use with {@link ConfigurationPolicy.REQUIRE} components;
	 * 
	 * use with {@link ConfigurationPolicy.OPTIONAL} components;
	 * 
	 * change persisted configuration for a singleton
	 * 
	 * calls scr.modified() with provided properties;
	 * 
	 * @param servicePid
	 *            - {@link #ComponentConstants.COMPONENT_NAME}
	 */
	boolean singletonUpdate(String servicePid, Map<String, String> properties);

	/**
	 * use with {@link ConfigurationPolicy.OPTIONAL} components;
	 * 
	 * remove persisted configuration for a singleton;
	 * 
	 * calls scr.modified() with empty props;
	 * 
	 * @param servicePid
	 *            - {@link #ComponentConstants.COMPONENT_NAME}
	 */
	boolean singletonRemove(String servicePid);

}
