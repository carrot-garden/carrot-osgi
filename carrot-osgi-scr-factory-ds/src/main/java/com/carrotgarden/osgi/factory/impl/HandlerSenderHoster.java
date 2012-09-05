/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.factory.api.FactoryHandler;
import com.carrotgarden.osgi.factory.api.FidgetManager;

@Component(immediate = true)
public class HandlerSenderHoster<M extends FidgetManager<?>> implements
		HandlerSender<M> {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<M, HandlerList<M>> handlerMap = new ConcurrentHashMap<M, HandlerList<M>>();

	@Activate
	protected void activate() {

	}

	@Deactivate
	protected void deactivate() {

	}

	private HandlerList<M> loadList(final M manager) {

		HandlerList<M> list = handlerMap.get(manager);

		if (list == null) {
			list = new HandlerList<M>();
			handlerMap.put(manager, list);
		}

		return list;
	}

	//

	@Reference(//
	cardinality = ReferenceCardinality.MULTIPLE, //
	policy = ReferencePolicy.DYNAMIC //
	)
	protected void bind(final FactoryHandler<M> handler) {

		if (handler == null) {
			log.error("handler == null", new Exception());
			return;
		}

		final M manager = handler.getBoundManager();

		if (manager == null) {
			log.error("manager == null", new Exception(handler.getClass()
					.getName()));
			return;
		}

		final HandlerList<M> list = loadList(manager);

		list.add(handler);

	}

	protected void unbind(final FactoryHandler<M> handler) {

		if (handler == null) {
			log.error("handler == null", new Exception());
			return;
		}

		final M manager = handler.getBoundManager();

		if (manager == null) {
			log.error("manager == null", new Exception(handler.getClass()
					.getName()));
			return;
		}

		final HandlerList<M> list = loadList(manager);

		list.remove(handler);

	}

	//

	@Override
	public void sendState(final M manager, final String factoryId,
			final boolean isOn) {

		if (manager == null) {
			log.error("manager == null", new Exception());
			return;
		}

		if (factoryId == null) {
			log.error("factoryId == null", new Exception());
			return;
		}

		final HandlerList<M> list = loadList(manager);

		for (final FactoryHandler<M> handler : list) {
			try {
				handler.handleFactory(factoryId, isOn);
			} catch (final Exception e) {
				log.error("invalid handler.handleFactory", e);
			}
		}

	}

}
