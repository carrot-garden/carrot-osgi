/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.conf.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.component.annotations.Component;

import com.carrotgarden.osgi.conf.api.ConfigAdminService;

@Component
public class ConfigAdminServiceProvider extends ConfigAdminCore implements
		ConfigAdminService {

	@Override
	public boolean isPresent(final String servicePid) {

		final String filter = //
		"(" + Constants.SERVICE_PID + "=" + servicePid + ")";

		return adminSearch(filter).size() > 0;

	}

	@Override
	public boolean isValid(final String servicePid) {
		return servicePid != null && servicePid != INVALID_PID;
	}

	@Override
	public List<String> listAll() {

		return adminSearch(null);

	}

	@Override
	public List<String> listMultiton(final String factoryId) {

		final String filter = //
		"(" + Constants.SERVICE_PID + "=" + factoryId + ".*" + ")";

		return adminSearch(filter);

	}

	@Override
	public List<String> listComponent(final String filter) {
		return adminSearch(filter);
	}

	@Override
	public List<String> listSingleton() {

		final List<String> list = new LinkedList<String>();

		try {

			final Configuration[] configArray = configList(null);

			if (configArray == null) {
				return list;
			}

			for (final Configuration config : configArray) {

				if (config.getFactoryPid() == null) {
					continue;
				}

				list.add(config.getPid());

			}

		} catch (final Exception e) {
			log.error("failed to list ", e);
		}

		return list;
	}

	@Override
	public String multitonCreate(final String factoryId,
			final Map<String, String> properties) {

		return adminCreate(factoryId, properties);

	}

	@Override
	public boolean multitonDestroy(final String servicePid) {

		return adminDelete(servicePid);

	}

	@Override
	public boolean multitonUpdate(final String servicePid,
			final Map<String, String> properties) {

		return adminUpdate(servicePid, properties);

	}

	@Override
	public Map<String, String> properties(final String servicePid) {

		try {

			return wrap(configProps(servicePid));

		} catch (final Exception e) {
			log.error("failed to retrieve properties", e);
			return EMPTY_PROPS;
		}

	}

	@Override
	public List<Map<String, String>> propertiesList(final String filter) {

		final List<String> pidList = adminSearch(filter);

		final List<Map<String, String>> propList = //
		new ArrayList<Map<String, String>>(pidList.size());

		for (final String servicePid : pidList) {
			propList.add(properties(servicePid));
		}

		return propList;

	}

	@Override
	public boolean singletonCreate(final String servicePid,
			final Map<String, String> properties) {

		return adminUpdate(servicePid, properties);

	}

	@Override
	public boolean singletonDestroy(final String servicePid) {

		return adminDelete(servicePid);

	}

	@Override
	public boolean singletonRemove(final String servicePid) {

		return adminDelete(servicePid);

	}

	@Override
	public boolean singletonUpdate(final String servicePid,
			final Map<String, String> properties) {

		return adminUpdate(servicePid, properties);

	}

}
