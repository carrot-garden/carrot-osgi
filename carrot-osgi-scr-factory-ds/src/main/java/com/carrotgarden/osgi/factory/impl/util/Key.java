/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Key<V> {

	private static Logger log = LoggerFactory.getLogger(Key.class);

	private final Class<V> type;

	public final Class<V> type() {
		return type;
	}

	private String name;

	public final String name() {

		if (name == null) {
			final Field[] fieldArray = getClass().getDeclaredFields();
			for (final Field field : fieldArray) {
				try {
					if (!Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					if (this == field.get(null)) {
						name = field.getName();
						break;
					}
				} catch (final Exception e) {
					log.error("", e);
				}
			}
		}
		return name;
	}

	private final V value;

	public final V value() {
		return value;
	}

	protected Key() {
		throw new UnsupportedOperationException("do not use");
	}

	@SuppressWarnings("unchecked")
	protected Key(final V value) {
		this.value = value;
		this.type = (Class<V>) value.getClass();
	}

}
