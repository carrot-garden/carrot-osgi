/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.conf.api.ConfigAdminService;

public class ConfigAdminCore {

	protected final static Map<String, String> EMPTY_PROPS = //
	Collections.emptyMap();

	private ConfigurationAdmin configAdmin;

	private ComponentContext context;

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected String adminCreate(final String factoryId,
			final Map<String, String> props) {

		try {

			final Configuration config = configMake(factoryId);

			config.update(wrap(props));

			final String servicePid = config.getPid();

			return servicePid;

		} catch (final Exception e) {

			log.error("failed to create config", e);

			return ConfigAdminService.INVALID_PID;

		}

	}

	protected boolean adminDelete(final String servicePid) {

		try {

			final Configuration config = configGet(servicePid);

			config.delete();

			return true;

		} catch (final Exception e) {
			log.error("failed to delete config", e);
			return false;
		}

	}

	protected List<String> adminSearch(final String filter) {

		try {

			final Configuration[] configArray = configList(filter);

			if (configArray == null) {
				return Collections.emptyList();
			}

			final List<String> list = new ArrayList<String>(configArray.length);

			for (final Configuration config : configArray) {
				list.add(config.getPid());
			}

			return list;

		} catch (final Exception e) {

			log.error("failed to list config", e);

			return Collections.emptyList();

		}

	}

	protected boolean adminUpdate(final String servicePid,
			final Map<String, String> props) {

		try {

			final Configuration config = configGet(servicePid);

			config.update(wrap(props));

			return true;

		} catch (final Exception e) {

			log.error("failed to update config ", e);

			return false;

		}

	}

	@Reference
	protected void bind(final ConfigurationAdmin s) {
		configAdmin = s;
	}

	@Activate
	protected void compActivate(final ComponentContext c) {

		log.debug("activate");

		context = c;

	}

	@Deactivate
	protected void compDeactivate(final ComponentContext c) {

		log.debug("deactivate");

		context = null;

	}

	protected Configuration configGet(final String servicePid) throws Exception {
		return configAdmin.getConfiguration(servicePid, null);
	}

	protected Configuration[] configList(final String filter) throws Exception {
		return configAdmin.listConfigurations(filter);
	}

	protected Configuration configMake(final String factoryId) throws Exception {
		return configAdmin.createFactoryConfiguration(factoryId, null);
	}

	protected Dictionary<Object, Object> configProps(final String servicePid)
			throws Exception {

		final Configuration config = configGet(servicePid);

		@SuppressWarnings("unchecked")
		final Dictionary<Object, Object> dict = config.getProperties();

		return dict;

	}

	protected ComponentContext context() {
		return context;
	}

	protected void unbind(final ConfigurationAdmin s) {
		configAdmin = null;
	}

	protected Map<String, String> wrap(final Dictionary<Object, Object> dict) {

		final Map<String, String> props = new HashMap<String, String>();

		if (dict == null) {
			return props;
		}

		final Enumeration<?> keys = dict.keys();

		while (keys.hasMoreElements()) {

			final Object key = keys.nextElement();
			final Object value = dict.get(key);

			if (key instanceof String && value instanceof String) {
				props.put(key.toString(), value.toString());
			}

		}

		return props;

	}

	protected Dictionary<Object, Object> wrap(final Map<String, String> props) {

		final Dictionary<Object, Object> dict = new Hashtable<Object, Object>();

		if (props == null) {
			return dict;
		}

		for (final Map.Entry<String, String> entry : props.entrySet()) {
			dict.put(entry.getKey(), entry.getValue());
		}

		return dict;

	}

}
