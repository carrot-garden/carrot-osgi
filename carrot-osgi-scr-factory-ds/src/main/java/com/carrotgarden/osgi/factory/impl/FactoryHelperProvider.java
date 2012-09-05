/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.factory.api.FactoryDescriptor;
import com.carrotgarden.osgi.factory.api.Fidget;

@Component(immediate = true)
public class FactoryHelperProvider implements FactoryHelper {

	private final Logger log = LoggerFactory.getLogger(getClass());

	//

	private ComponentContext componentContext;

	@Activate
	protected void activate(final ComponentContext c) {
		componentContext = c;
	}

	@Deactivate
	protected void deactivate(final ComponentContext c) {
		componentContext = null;
	}

	private BundleContext bundleContext() {
		return componentContext.getBundleContext();
	}

	//

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Bundle serviceBundle(final Class<?> klaz, final Object instance)
			throws Exception {

		final BundleContext context = bundleContext();

		final ServiceReference[] referenceArray = context
				.getAllServiceReferences(klaz.getName(), null);

		for (final ServiceReference reference : referenceArray) {

			final Object service = context.getService(reference);

			if (service == instance) {

				final Bundle bundle = reference.getBundle();

				return bundle;

			}

		}

		log.error("bundle not found : klaz={} instance={}", klaz, instance);

		return null;

	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private Object serviceProperty(final Class<?> klaz, final Object instance,
			final String propName) throws Exception {

		final BundleContext context = bundleContext();

		final ServiceReference[] referenceArray = context
				.getAllServiceReferences(klaz.getName(), null);

		for (final ServiceReference reference : referenceArray) {

			final Object service = context.getService(reference);

			if (service == instance) {

				final Object propValue = reference.getProperty(propName);

				return propValue;

			}

		}

		log.error("property not found : {} {}", instance, propName);

		return null;

	}

	private final Map<String, String> EMPTY_PROPS = //
	Collections.unmodifiableMap(new HashMap<String, String>());

	private Map<String, String> factoryDescriptor(final Class<?> klaz)
			throws Exception {

		final Field[] fieldArray = klaz.getDeclaredFields();

		for (final Field field : fieldArray) {

			final boolean isDescriptor = field
					.isAnnotationPresent(FactoryDescriptor.class);

			if (isDescriptor) {

				field.setAccessible(true);

				final int modifiers = field.getModifiers();

				final boolean isStatic = (modifiers & Modifier.STATIC) > 0;
				final boolean isFinal = (modifiers & Modifier.FINAL) > 0;

				if (!(isStatic && isFinal)) {
					throw new Exception(
							"factory descriptor field must be 'static' and  'final' ");
				}

				final Object descriptor = field.get(null);

				if (!(descriptor instanceof Map)) {
					throw new Exception("factory descriptor is not a valid map");
				}

				final Map<?, ?> source = (Map<?, ?>) descriptor;

				final Map<String, String> target = new HashMap<String, String>();

				for (final Entry<?, ?> entry : source.entrySet()) {

					final Object key = entry.getKey();
					final Object value = entry.getValue();

					if (key == null || value == null) {
						log.error("factory descriptor map has invalid entry",
								new Exception(klaz.getName()));
						continue;
					}

					target.put(key.toString(), value.toString());

				}

				return Collections.unmodifiableMap(target);

			}

		}

		return EMPTY_PROPS;

	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void logServiceProperties(final Class<?> klaz, final Object instance)
			throws Exception {

		final BundleContext context = bundleContext();

		final ServiceReference[] referenceArray = context
				.getAllServiceReferences(klaz.getName(), null);

		for (final ServiceReference reference : referenceArray) {

			final Object service = context.getService(reference);

			if (service == instance) {

				log.info("instance=" + instance);

				final String[] propKeyArray = reference.getPropertyKeys();

				for (final String propKey : propKeyArray) {

					final Object propValue = reference.getProperty(propKey);

					log.info(propKey + "=" + propValue);

				}

				// final String[] nameArray = (String[]) reference
				// .getProperty("objectClass");
				// for (final String name : nameArray) {
				// log.info("name=" + name);
				// }

			}

		}

	}

	// ##################################

	/** agreement: component.factory is set by user as factory UUID */
	@Override
	public String factoryId(final FactoryContext factoryContext) {

		final String factoryId = factoryContext.factoryProps
				.get(Const.PROP_COMP_FACTORY);

		return factoryId;

	}

	/** contract: component.name is set by SCR to component class name */
	@Override
	public Class<? extends Fidget> fidgetClass(
			final FactoryContext factoryContext) throws Exception {

		final Bundle serviceBundle = serviceBundle(ComponentFactory.class,
				factoryContext.factoryOSGI);

		final String componentName = factoryContext.factoryProps
				.get(Const.PROP_COMP_NAME);

		@SuppressWarnings("unchecked")
		final Class<? extends Fidget> klaz = //
		(Class<? extends Fidget>) serviceBundle.loadClass(componentName);

		return klaz;

	}

	/** contract: factory descriptor is user annotated static final map */
	@Override
	public Map<String, String> fidgetDescriptor(
			final FactoryContext factoryContext) throws Exception {

		final Class<?> klaz = fidgetClass(factoryContext);

		final Map<String, String> map = factoryDescriptor(klaz);

		return map;

	}

	@Override
	public void logFactoryProps(final FactoryContext factoryContext) {

		log.info("factory : {}", factoryId(factoryContext));

		final Map<String, String> props = factoryContext.factoryProps;

		for (final Map.Entry<String, String> entry : props.entrySet()) {

			log.info(entry.getKey() + "=" + entry.getValue());

		}

	}

}
