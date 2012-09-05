/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.conf.api.ConfigAdminService;
import com.carrotgarden.osgi.factory.api.Cidget;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CidgetManagerCore<C extends Cidget> implements
		ServiceTrackerCustomizer {

	protected static final long DEFAULT_TIMEOUT = Long
			.parseLong(Cidget.DEFAULT_TIMEOUT);

	private ComponentContext componentContext;

	private ConfigAdminService configAdmin;

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private ServiceTracker serviceTracker;

	/** instance set by tracker add/remove/modify operation */
	private C trackerInstance;

	/** tracker add/remove/modify operation guard */
	private final Object trackerLock = new Object();

	/** tracker interaction handshake */
	protected Object trackerLock() {
		return trackerLock;
	}

	@Activate
	protected void activate(final ComponentContext c) {

		componentContext = c;

		serviceTracker = new ServiceTracker( //
				bundleContext(), interfaceName(), this);

		serviceTracker().open(true);

	}

	@Reference
	protected void bind(final ConfigAdminService s) {
		configAdmin = s;
	}

	protected BundleContext bundleContext() {
		return componentContext.getBundleContext();
	}

	protected ComponentContext componentContext() {
		return componentContext;
	}

	protected ConfigAdminService configAdmin() {
		return configAdmin;
	}

	@Deactivate
	protected void deactivate() {

		serviceTracker().close();

		serviceTracker = null;

		componentContext = null;

	}

	protected void ensureServiceMissing(final String instanceId)
			throws Exception {

		final ServiceReference reference = serviceReference(instanceId);

		if (reference != null) {
			throw new Exception("duplicate instance id = " + instanceId);
		}

	}

	protected String ensureServicePresent(final String instanceId)
			throws Exception {

		final ServiceReference reference = serviceReference(instanceId);

		if (reference == null) {
			throw new Exception("invalid instance id");
		}

		final String servicePid = (String) reference
				.getProperty(Constants.SERVICE_PID);

		if (servicePid == null) {
			throw new Exception("invalid service pid");
		}

		return servicePid;

	}

	protected ServiceReference serviceReference(final String instanceId)
			throws Exception {

		if (instanceId == null) {
			throw new Exception("missing instance id");
		}

		final List<ServiceReference> referenceList = referenceList(
				interfaceName(), filterInstanceId(instanceId));

		switch (referenceList.size()) {
		case 0:
			return null;
		case 1:
			return referenceList.get(0);
		default:
			throw new Exception("duplicate instance id : " + instanceId);
		}

	}

	protected List<ServiceReference> referenceList(final String className,
			final String filterExpression) throws Exception {

		final ServiceReference[] referenceArray = bundleContext()
				.getServiceReferences(className, filterExpression);

		if (referenceArray == null) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(referenceArray);
		}

	}

	protected Class<C> interfaceClass() {
		throw new UnsupportedOperationException("expecting override");
	}

	protected String interfaceName() {
		return interfaceClass().getName();
	}

	/** @return operation timeout value */
	protected long operationTimeout(final Map<String, String> properties) {
		if (properties == null) {
			return DEFAULT_TIMEOUT;
		}
		final String value = properties.get(Cidget.PROP_TIMEOUT);
		if (value == null) {
			return DEFAULT_TIMEOUT;
		}
		try {
			return Long.parseLong(value);
		} catch (final Exception e) {
			return DEFAULT_TIMEOUT;
		}
	}

	protected ServiceTracker serviceTracker() {
		return serviceTracker;
	}

	/** tracker service interaction instance get/set replace */
	protected C trackerInstance(final C next) {
		final C past = trackerInstance;
		trackerInstance = next;
		return past;
	}

	protected void instanceNotify(final ServiceReference reference,
			final Object service) {

		synchronized (trackerLock()) {

			try {

				final String instanceId = (String) reference
						.getProperty(Cidget.PROP_INSTANCE_ID);

				if (instanceId == null) {
					throw new Exception("missing instance id");
				}

				trackerInstance((C) service);

			} catch (final Exception e) {

				log.error("tracker failure", e);

			} finally {

				trackerLock().notifyAll();

			}

		}

	}

	protected void unbind(final ConfigAdminService s) {
		configAdmin = null;
	}

	@Override
	public Object addingService(final ServiceReference reference) {

		/** change bundle/service registration count */
		final Object service = bundleContext().getService(reference);

		instanceNotify(reference, service);

		return service;

	}

	@Override
	public void modifiedService(final ServiceReference reference,
			final Object service) {

		instanceNotify(reference, service);

	}

	@Override
	public void removedService(final ServiceReference reference,
			final Object service) {

		/** change bundle/service registration count */
		bundleContext().ungetService(reference);

		instanceNotify(reference, service);

	}

	protected String instanceId(final Map<String, String> props) {
		return props.get(Cidget.PROP_INSTANCE_ID);
	}

	protected String filterInstanceId(final String instanceId) {
		return "(" + Cidget.PROP_INSTANCE_ID + "=" + instanceId + ")";
	}

	protected String filterClassName(final String className) {
		return "(" + Constants.OBJECTCLASS + "=" + className + ")";
	}

}
