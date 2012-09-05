/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.sling.commons.threads.ThreadPool;
import org.apache.sling.commons.threads.ThreadPoolManager;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.event.api.EventAdminService;
import com.carrotgarden.osgi.event.api.EventUtil;
import com.carrotgarden.osgi.factory.api.FactoryEvent;
import com.carrotgarden.osgi.factory.api.FactoryHandler;
import com.carrotgarden.osgi.factory.api.Fidget;
import com.carrotgarden.osgi.factory.api.FidgetManager;
import com.carrotgarden.osgi.factory.api.FidgetManagerBase;
import com.carrotgarden.osgi.factory.impl.util.Props;

/**
 * 
 * factory for factory
 * 
 */
@Component(factory = FidgetManagerBase.FACTORY)
public class FidgetManagerProvider<F extends Fidget> implements
		FidgetManager<F> {

	private static final String POOL_NAME = FidgetManagerProvider.class
			.getName();

	protected final Logger log = LoggerFactory.getLogger(getClass());

	//

	/** factory cache : [factoryId, factory wrapper] */
	private final Map<String, FactoryInstance> factoryMap = new ConcurrentHashMap<String, FactoryInstance>();

	/** instance cache : [instanceId, instance wrapper] */
	private final Map<String, ComponentInstance> instanceMap = new ConcurrentHashMap<String, ComponentInstance>();

	/** factory deferred bind registration queue */
	private final BlockingQueue<FactoryContext> bindQueue = new LinkedBlockingQueue<FactoryContext>();

	//

	/**  */
	private volatile Class<F> interfaceClass;

	/**
	 * @return factory bind/unbind filter interface
	 */
	private Class<F> getFidgetInterface() {
		return interfaceClass;
	}

	//

	private boolean isActive;

	/** @return are we inside activate/deactivate stage ? */
	protected final boolean isActive() {
		return isActive;
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected final void activate(final ComponentContext context)
			throws Exception {

		threadPool = threadPoolManager.get(POOL_NAME);

		final Dictionary<String, String> props = context.getProperties();

		final String bundleId = props.get(FidgetManagerBase.PROP_BUNDLE_ID);

		final String interfaceName = props
				.get(FidgetManagerBase.PROP_INTERFACE);

		final long id = Long.parseLong(bundleId);

		final Bundle bundle = context.getBundleContext().getBundle(id);

		interfaceClass = (Class<F>) bundle.loadClass(interfaceName);

		//

		synchronized (this) {
			isActive = true;
		}

		final List<FactoryContext> bindList = new LinkedList<FactoryContext>();

		bindQueue.drainTo(bindList);

		for (final FactoryContext serviceContext : bindList) {
			execute(new TaskBind(serviceContext));
		}

		log.debug("activate manager for : {}", getFidgetInterface());

	}

	@Deactivate
	protected final void deactivate(final ComponentContext context) {

		for (final FactoryInstance factory : factoryMap.values()) {
			// blocking
			new TaskUnbind(factory.factoryContext).run();
		}

		factoryMap.clear();

		//

		for (final ComponentInstance wrapper : instanceMap.values()) {
			// blocking
			wrapper.dispose();
		}

		instanceMap.clear();

		//

		synchronized (this) {
			isActive = false;
		}

		threadPoolManager.release(threadPool);

		interfaceClass = null;

		log.debug("deactivate manager for : {}", getFidgetInterface());

	}

	//

	/**
	 * @return valid factory or null for invalid conditions
	 */
	protected final FactoryInstance discoverNew(final FactoryContext context) {

		try {

			final Class<F> fidgetInterface = getFidgetInterface();

			final Class<?> fidgetClass = helper.fidgetClass(context);

			final boolean isManagedHere = //
			fidgetInterface.isAssignableFrom(fidgetClass);

			if (!isManagedHere) {
				return null;
			}

			final String factoryId = helper.factoryId(context);

			final Map<String, String> factoryDescriptor = helper
					.fidgetDescriptor(context);

			final FactoryInstance factoryNew = new FactoryInstance(factoryId,
					fidgetClass, factoryDescriptor, context);

			return factoryNew;

		} catch (final Exception e) {

			helper.logFactoryProps(context);

			log.error("discover failed", e);

			return null;

		}

	}

	protected final FactoryInstance discoverOld(final FactoryContext context) {

		final String factoryId = helper.factoryId(context);

		return factoryMap.get(factoryId);

	}

	private class TaskBind implements Runnable {

		private final FactoryContext serviceContext;

		TaskBind(final FactoryContext serviceContext) {
			this.serviceContext = serviceContext;
		}

		@Override
		public void run() {

			final FactoryInstance factory = discoverNew(serviceContext);

			if (factory == null) {
				// error or foreign
				return;
			}

			final String factoryId = factory.factoryId;

			if (hasFactory(factoryId)) {
				log.error("duplicate factory id", new Exception(factoryId));
				return;
			}

			log.debug("factory bind   : {}", factory);

			factoryMap.put(factoryId, factory);

			final Props props = Props.make();
			props.put(FactoryEvent.PROP_FACTORY_ID, factoryId);
			eventer.send(FactoryEvent.FACTORY_ENABLED, props);

			sender.sendState(FidgetManagerProvider.this, factoryId,
					FactoryHandler.ON);

		}
	};

	private class TaskUnbind implements Runnable {

		private final FactoryContext serviceContext;

		TaskUnbind(final FactoryContext serviceContext) {
			this.serviceContext = serviceContext;
		}

		@Override
		public void run() {

			final FactoryInstance factory = discoverOld(serviceContext);

			if (factory == null) {
				// missing or foreign
				return;
			}

			final String factoryId = factory.factoryId;

			if (!hasFactory(factoryId)) {
				log.error("unknown factory id", new Exception(factoryId));
				return;
			}

			log.debug("factory unbind : {}", factory);

			final Props props = Props.make();
			props.put(FactoryEvent.PROP_FACTORY_ID, factoryId);
			eventer.send(FactoryEvent.FACTORY_DISABLED, props);

			sender.sendState(FidgetManagerProvider.this, factoryId,
					FactoryHandler.OFF);

			factoryMap.remove(factoryId);

		}
	};

	protected final void execute(final Runnable task) {

		final ThreadPool exec = threadPool;

		if (exec == null) {
			log.debug("executor is unbound");
		} else {
			exec.execute(task);
		}

	}

	@Override
	public List<String> getFactoryIds() {

		return new ArrayList<String>(factoryMap.keySet());

	}

	@Override
	public final F create(final String factoryId,
			final Map<String, String> props) {

		if (factoryId == null) {
			log.error("factoryId==null", new Exception());
			return null;
		}

		final FactoryInstance factoryWrapper = factoryMap.get(factoryId);

		if (factoryWrapper == null) {
			log.error("unknown factory id", new Exception(factoryId));
			return null;
		}

		final ComponentInstance instanceWrapper;

		try {
			instanceWrapper = factoryWrapper.newInstance(props);
		} catch (final Exception e) {
			log.error("factory can not produce instance", new Exception(
					factoryId));
			return null;
		}

		@SuppressWarnings("unchecked")
		final F instance = (F) instanceWrapper.getInstance();
		if (instance == null) {
			log.error("instance == null", new Exception(factoryId));
			instanceWrapper.dispose();
			return null;
		}

		final String instanceId;

		try {
			instanceId = instance.getInstanceId();
		} catch (final Exception e) {
			log.error("invalid instanceId", new Exception(factoryId));
			instanceWrapper.dispose();
			return null;
		}

		if (instanceId == null) {
			log.error("instanceId == null", new Exception(factoryId));
			instanceWrapper.dispose();
			return null;
		}

		if (instanceMap.containsKey(instanceId)) {
			log.error("duplicate instance id", new Exception(instanceId));
			instanceWrapper.dispose();
			return null;
		}

		instanceMap.put(instanceId, instanceWrapper);

		log.debug("created : {}", instance);

		return instance;

	}

	@Override
	public final boolean destroy(final F fidget) {

		if (fidget == null) {
			log.error("fidget == null", new Exception());
			return false;
		}

		final String instanceId;

		try {
			instanceId = fidget.getInstanceId();
		} catch (final Exception e) {
			log.error("invalid fidget.getInstanceId", e);
			return false;
		}

		if (instanceId == null) {
			log.error("instanceId == null", new Exception());
			return false;
		}

		final ComponentInstance instanceWrapper = instanceMap.remove(instanceId);
		if (instanceWrapper == null) {
			log.error("unknown instance id", new Exception(instanceId));
			return false;
		}

		final Object instance = instanceWrapper.getInstance();
		if (instance == null) {
			log.error("instance == null", new Exception(instanceId));
			return false;
		}

		instanceWrapper.dispose();

		log.debug("destroyed : {}", fidget);

		return true;

	}

	//

	@Override
	public final boolean hasFactory(final String factoryId) {

		return factoryMap.containsKey(factoryId);

	}

	//

	@Override
	public final boolean hasInstance(final String instanceId) {

		return instanceMap.containsKey(instanceId);

	}

	@Override
	public final F getInstance(final String instanceId) {

		final ComponentInstance instanceWrapper = instanceMap.get(instanceId);

		if (instanceWrapper == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		final F fidget = (F) instanceWrapper.getInstance();

		return fidget;

	}

	@Override
	public final List<F> getInstances() {

		final List<F> list = new ArrayList<F>(instanceMap.size());

		for (final ComponentInstance wrapper : instanceMap.values()) {

			@SuppressWarnings("unchecked")
			final F fidget = (F) wrapper.getInstance();

			if (fidget == null) {
				// destroyed
				continue;
			}

			list.add(fidget);

		}

		return list;

	}

	@Override
	public final List<F> getInstances(final String factoryId) {

		final List<F> list = new LinkedList<F>();

		if (factoryId == null) {
			log.error("factoryId == null", new Exception());
			return list;
		}

		final FactoryInstance factory = factoryMap.get(factoryId);

		if (factory == null) {
			log.error("unknown factory id", new Exception(factoryId));
			return list;
		}

		final Class<?> fidgetClass = factory.fidgetClass;

		if (fidgetClass == null) {
			log.error("fidgetClass == null", new Exception(factoryId));
			return list;
		}

		for (final ComponentInstance wrapper : instanceMap.values()) {

			@SuppressWarnings("unchecked")
			final F fidget = (F) wrapper.getInstance();

			if (fidget == null) {
				// destoyed
				continue;
			}

			if (fidgetClass == fidget.getClass()) {
				list.add(fidget);
			}

		}

		return list;

	}

	@Override
	public final boolean hasFiredEnabled(final Event event) {

		return hasFired(event, FactoryEvent.FACTORY_ENABLED);

	}

	@Override
	public final boolean hasFiredDisabled(final Event event) {

		return hasFired(event, FactoryEvent.FACTORY_DISABLED);

	}

	private final boolean hasFired(final Event event, final String name) {

		if (event == null) {
			log.error("event == null", new Exception());
			return false;
		}

		if (!EventUtil.is(event, name)) {
			// non manager event
			return false;
		}

		final String factoryId = EventUtil.getProperty(event,
				FactoryEvent.PROP_FACTORY_ID);

		if (factoryId == null) {
			log.error("factoryId == null", new Exception());
			return false;
		}

		return hasFactory(factoryId);

	}

	@Override
	public final String getFactoryId(final Event event) {

		if (event == null) {
			log.error("event == null", new Exception());
			return null;
		}

		final String factoryId = EventUtil.getProperty(event,
				FactoryEvent.PROP_FACTORY_ID);

		if (factoryId == null) {
			// non manager event
			return null;
		}

		if (hasFactory(factoryId)) {
			return factoryId;
		} else {
			return null;
		}

	}

	//

	@Override
	public Map<String, String> getFactoryDescriptor(final String factoryId) {

		if (factoryId == null) {
			log.error("factoryId == null", new Exception());
			return null;
		}

		final FactoryInstance factory = factoryMap.get(factoryId);

		if (factory == null) {
			// destroyed
			return null;
		}

		final Map<String, String> map = factory.factoryDescriptor;

		if (map == null) {
			// not provided
			return null;
		}

		return map;

	}

	// ######################################

	//

	private FactoryHelper helper;

	@Reference
	protected void bind(final FactoryHelper h) {
		helper = h;
	}

	protected void unbind(final FactoryHelper h) {
		helper = null;
	}

	//

	private ThreadPool threadPool;

	private ThreadPoolManager threadPoolManager;

	@Reference
	protected void bind(final ThreadPoolManager s) {
		threadPoolManager = s;
	}

	protected void unbind(final ThreadPoolManager s) {
		threadPoolManager = null;
	}

	//

	private EventAdminService eventer;

	@Reference
	protected void bind(final EventAdminService e) {
		eventer = e;
	}

	protected void unbind(final EventAdminService e) {
		eventer = null;
	}

	//

	private HandlerSender<FidgetManager<F>> sender;

	@Reference
	protected void bind(final HandlerSender<FidgetManager<F>> s) {
		sender = s;
	}

	protected void unbind(final HandlerSender<FidgetManager<F>> s) {
		sender = null;
	}

	//

	@Reference( //
	cardinality = ReferenceCardinality.MULTIPLE, //
	policy = ReferencePolicy.DYNAMIC //
	)
	protected final void bind(final ComponentFactory factoryOSGI,
			final Map<String, String> factoryProps) {

		final FactoryContext serviceContext = new FactoryContext(factoryOSGI,
				factoryProps);

		synchronized (this) {
			if (isActive()) {
				execute(new TaskBind(serviceContext));
			} else {
				try {
					bindQueue.put(serviceContext);
				} catch (final Exception e) {
					log.error("unexpected", e);
				}
			}
		}

	}

	protected final void unbind(final ComponentFactory factoryOSGI,
			final Map<String, String> factoryProps) {

		final FactoryContext serviceContext = new FactoryContext(factoryOSGI,
				factoryProps);

		synchronized (this) {
			if (isActive()) {
				execute(new TaskUnbind(serviceContext));
			}
		}

	}

}
