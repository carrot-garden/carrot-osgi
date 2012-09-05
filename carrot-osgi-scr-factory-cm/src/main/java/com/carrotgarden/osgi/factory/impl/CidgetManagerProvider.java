/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.framework.ServiceReference;

import com.carrotgarden.osgi.factory.api.Cidget;
import com.carrotgarden.osgi.factory.api.CidgetManager;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CidgetManagerProvider<C extends Cidget> extends
		CidgetManagerCore<C> implements CidgetManager<C> {

	@Override
	public Map<String, String> config(final String instanceId) {

		final List<String> list = configAdmin().listComponent(
				filterInstanceId(instanceId));

		if (list.isEmpty()) {
			return Collections.emptyMap();
		}

		final String servicePid = list.get(0);

		return configAdmin().properties(servicePid);

	}

	@Override
	public boolean configCreate(final String factoryId,
			final Map<String, String> properties) {

		final String instanceId = instanceId(properties);

		if (instanceId == null) {
			log.error("missing property",
					new Exception(Cidget.PROP_INSTANCE_ID));
			return false;
		}

		final List<String> list = configAdmin().listComponent(
				filterInstanceId(instanceId));

		if (!list.isEmpty()) {
			log.error("duplicate configuration", new Exception(instanceId));
			return false;
		}

		final String servicePid = configAdmin().multitonCreate(factoryId,
				properties);

		return configAdmin().isValid(servicePid);

	}

	@Override
	public boolean configDestroy(final String instanceId) {

		final List<String> list = configAdmin().listComponent(
				filterInstanceId(instanceId));

		if (list.isEmpty()) {
			log.error("configuration not present", new Exception(instanceId));
			return false;
		}

		final String servicePid = list.get(0);

		return configAdmin().multitonDestroy(servicePid);

	}

	@Override
	public boolean configEnsure(final String factoryId,
			final Map<String, String> properties) {

		final String instanceId = instanceId(properties);

		if (instanceId == null) {
			log.error("missing property",
					new Exception(Cidget.PROP_INSTANCE_ID));
			return false;
		}

		if (hasConfig(instanceId)) {
			return configUpdate(instanceId, properties);
		} else {
			return configCreate(factoryId, properties);
		}

	}

	@Override
	public List<Map<String, String>> configList() {

		return configAdmin().propertiesList(filterClassName(interfaceName()));

	}

	@Override
	public boolean configUpdate(final String instanceId,
			final Map<String, String> properties) {

		if (instanceId(properties) == null) {
			log.error("missing property",
					new Exception(Cidget.PROP_INSTANCE_ID));
			return false;
		}

		final List<String> list = configAdmin().listComponent(
				filterInstanceId(instanceId));

		if (list.isEmpty()) {
			log.error("configuration not present", new Exception(instanceId));
			return false;
		}

		final String servicePid = list.get(0);

		return configAdmin().multitonUpdate(servicePid, properties);

	}

	@Override
	public boolean hasConfig(final String instanceId) {

		final List<String> list = configAdmin().listComponent(
				filterInstanceId(instanceId));

		return list.size() == 1;

	}

	@Override
	public boolean hasInstance(final String instanceId) {
		try {
			return serviceReference(instanceId) != null;
		} catch (final Exception e) {
			return false;
		}
	}

	@Override
	public C instance(final String instanceId) {

		try {

			final ServiceReference reference = serviceReference(instanceId);

			if (reference == null) {
				return null;
			}

			/** doses not affect bundle/service registration count */
			final C instance = (C) serviceTracker().getService(reference);

			return instance;

		} catch (final Exception e) {

			log.error("lookup failure", e);

			return null;

		}

	}

	@Override
	public C instanceCreate(final String factoryId,
			final Map<String, String> properties) {

		String servicePid = null;

		try {

			synchronized (trackerLock()) {

				ensureServiceMissing(instanceId(properties));

				/** initiate #@Activate */
				servicePid = configAdmin()
						.multitonCreate(factoryId, properties);

				trackerLock().wait(operationTimeout(properties));

				final C instance = trackerInstance(null);

				if (instance == null) {
					throw new Exception("instance @Activate timeout");
				}

				return instance;

			}

		} catch (final Exception e) {

			log.error("create failure", e);

			/** roll back */
			if (servicePid != null) {
				configAdmin().multitonDestroy(servicePid);
			}

			return null;

		}

	}

	@Override
	public boolean instanceDestroy(final String instanceId) {

		try {

			synchronized (trackerLock()) {

				final String servicePid = ensureServicePresent(instanceId);

				/** initiate #@Deactivate */
				configAdmin().multitonDestroy(servicePid);

				trackerLock().wait(operationTimeout(null));

				final C instance = trackerInstance(null);

				if (instance == null) {
					throw new Exception("instance @Deactivate timeout");
				}

				return true;

			}

		} catch (final Exception e) {

			log.error("destory failure", e);

			/** no roll back */

			return false;

		}

	}

	@Override
	public C instanceEnsure(final String factoryId,
			final Map<String, String> properties) {

		final String instanceId = instanceId(properties);

		if (instanceId == null) {
			log.error("missing property",
					new Exception(Cidget.PROP_INSTANCE_ID));
			return null;
		}

		if (hasInstance(instanceId)) {
			if (instanceUpdate(instanceId, properties)) {
				return instance(instanceId);
			} else {
				return null;
			}
		} else {
			return instanceCreate(instanceId, properties);
		}

	}

	@Override
	public List<C> instanceList() {

		try {

			final List<ServiceReference> referenceList = referenceList(
					interfaceName(), null);

			final List<C> instancelist = new ArrayList<C>(referenceList.size());

			for (final ServiceReference reference : referenceList) {

				/** doses not affect bundle/service registration count */
				final C instance = (C) serviceTracker().getService(reference);

				instancelist.add(instance);

			}

			return instancelist;

		} catch (final Exception e) {

			log.error("lookup failure", e);

			return Collections.emptyList();

		}

	}

	@Override
	public boolean instanceUpdate(final String instanceId,
			final Map<String, String> properties) {

		try {

			synchronized (trackerLock()) {

				final String servicePid = ensureServicePresent(instanceId);

				/** initiate #@Modified */
				configAdmin().multitonUpdate(servicePid, properties);

				trackerLock().wait(operationTimeout(properties));

				final C instance = trackerInstance(null);

				if (instance == null) {
					throw new Exception("instance @Modified timeout");
				}

				return true;

			}

		} catch (final Exception e) {

			log.error("update failed", e);

			/** no roll back */

			return false;

		}

	}

}
