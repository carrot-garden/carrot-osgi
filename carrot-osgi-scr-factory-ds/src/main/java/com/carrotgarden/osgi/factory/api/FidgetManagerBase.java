/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.api;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;

/**
 * Declarative Services Factory Component manager implementation base class;
 * 
 * derive your factory management implementations from this
 */
@Component(immediate = true)
public abstract class FidgetManagerBase<F extends Fidget> implements
		FidgetManager<F> {

	protected abstract Class<F> getFidgetInterface();

	//

	/** provider factory */
	public static final String FACTORY = "carrot.factory.09f76a6c-d350-4b34-a74d-232376f9e5a5";

	/** consumer bundle id */
	public static final String PROP_BUNDLE_ID = "carrot.factory.component.bundleId";

	/** consumer component interface */
	public static final String PROP_INTERFACE = "carrot.factory.component.interface";

	//

	private ComponentFactory factory;

	/** bind only to provider factory */
	@Reference(target = "(component.factory=" + FACTORY + ")")
	protected final void bind(final ComponentFactory f) {
		factory = f;
	}

	protected final void unbind(final ComponentFactory f) {
		factory = null;
	}

	//

	/** this component contex */
	private ComponentContext context;

	/** provider wrapper */
	private ComponentInstance wrapper;

	@SuppressWarnings("unchecked")
	private FidgetManager<F> provider() {
		return (FidgetManager<F>) wrapper.getInstance();
	}

	@Activate
	protected void activate(final ComponentContext c) {

		context = c;

		final String bundleId = Long.toString(context.getBundleContext()
				.getBundle().getBundleId());

		final String interfaceName = getFidgetInterface().getName();

		final Dictionary<String, String> props = new Hashtable<String, String>();

		props.put(PROP_BUNDLE_ID, bundleId);
		props.put(PROP_INTERFACE, interfaceName);

		wrapper = factory.newInstance(props);

	}

	@Deactivate
	protected void deactivate(final ComponentContext c) {

		wrapper.dispose();

		context = null;

	}

	//

	/** proxy via provider */

	@Override
	public Map<String, String> getFactoryDescriptor(final String factoryId) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return null;
		}
		return provider.getFactoryDescriptor(factoryId);
	}

	@Override
	public String getFactoryId(final Event event) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return null;
		}
		return provider.getFactoryId(event);
	}

	@Override
	public boolean hasFiredEnabled(final Event event) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return false;
		}
		return provider.hasFiredEnabled(event);
	}

	@Override
	public boolean hasFiredDisabled(final Event event) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return false;
		}
		return provider.hasFiredDisabled(event);
	}

	@Override
	public boolean hasFactory(final String factoryId) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return false;
		}
		return provider.hasFactory(factoryId);
	}

	@Override
	public List<String> getFactoryIds() {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return new ArrayList<String>();
		}
		return provider.getFactoryIds();
	}

	@Override
	public boolean hasInstance(final String instanceId) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return false;
		}
		return provider.hasInstance(instanceId);
	}

	@Override
	public F getInstance(final String instanceId) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return null;
		}
		return provider.getInstance(instanceId);
	}

	@Override
	public List<F> getInstances() {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return new ArrayList<F>();
		}
		return provider.getInstances();
	}

	@Override
	public List<F> getInstances(final String factoryId) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return new ArrayList<F>();
		}
		return provider.getInstances(factoryId);
	}

	@Override
	public F create(final String factoryId,
			final Map<String, String> fidgetProps) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return null;
		}
		return provider.create(factoryId, fidgetProps);
	}

	@Override
	public boolean destroy(final F fidget) {
		final FidgetManager<F> provider = provider();
		if (provider == null) {
			return false;
		}
		return provider.destroy(fidget);
	}

}
