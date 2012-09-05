/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.impl.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * platform properties map
 */
@SuppressWarnings("serial")
public class Props extends HashMap<String, String> {

	private static Logger log = LoggerFactory.getLogger(Props.class);

	/**
	 * add any-type map non null entries, by converting them to string;
	 */
	public void putAllAny(final Map<?, ?> map) {

		if (map == null || map.size() == 0) {
			return;
		}

		for (final Map.Entry<?, ?> entry : map.entrySet()) {

			final Object key = entry.getKey();
			final Object value = entry.getValue();

			if (key == null || value == null) {
				continue;
			}

			put(key.toString(), value.toString());

		}

	}

	public Props with(final String key, final String value) {
		put(key, value);
		return this;
	}

	// public Props withJson(final String key, final Object value) {
	// put(key, JsonUtil.intoJson(value));
	// return this;
	// }

	public Map<String, String> immutable() {
		final Map<String, String> map = Collections.unmodifiableMap(this);
		return map;
	}

	public void log() {

		for (final Map.Entry<String, String> entry : this.entrySet()) {

			final String key = entry.getKey();
			final String value = entry.getValue();

			log.debug("\t {}={}", key, value);

		}

	}

	//

	@SuppressWarnings("unchecked")
	public <V> V get(final Key<V> key) {

		final String name = key.name();

		final String text = get(name);

		if (text == null) {
			return key.value();
		}

		final Class<V> klaz = key.type();

		if (klaz == Integer.class) {
			return (V) new Integer(text);
		}

		if (klaz == Double.class) {
			return (V) new Double(text);
		}

		if (klaz == String.class) {
			return (V) text;
		}

		log.error("unknown key type", new Exception(name));

		return null;

	}

	public <V> void put(final Key<V> key, final V value) {

		put(key.name(), value.toString());

	}

	//

	public static Props form(final Map<String, String> map) {
		final Props props = new Props();
		props.putAll(map);
		return props;
	}

	public static Props make() {
		return new Props();
	}

	public static Props make(final String key, final String value) {
		final Props props = new Props();
		props.put(key, value);
		return props;
	}

	// public static Props makeJson(final String key, final Object value) {
	// final Props props = new Props();
	// props.put(key, JsonUtil.intoJson(value));
	// return props;
	// }

	/** make new props form any type map by rendering to string */
	public static Props formAny(final Map<?, ?> map) {
		final Props props = new Props();
		props.putAllAny(map);
		return props;
	}

}
