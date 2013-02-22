/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.conv;

/**
 * Supported property types in {@link Component#property(}.
 */
public enum PropertyType {

	STRING(String.class), // default

	LONG(Long.class), //

	DOUBLE(Double.class), //

	FLOAT(Float.class), //

	INTEGER(Integer.class), //

	BYTE(Byte.class), //

	CHARACTER(Character.class), //

	BOOLEAN(Boolean.class), //

	SHORT(Short.class), //

	;

	public final Class<?> klaz;

	public final String value;

	private PropertyType(final Class<?> klaz) {
		this.klaz = klaz;
		this.value = klaz.getSimpleName();
	}

	private static final PropertyType[] ENUM_VALS = values();

	public static PropertyType from(final String value) {

		for (final PropertyType known : ENUM_VALS) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}

		return STRING;

	}

	public static PropertyType from(final Class<?> klaz) {

		for (final PropertyType known : ENUM_VALS) {
			if (known.klaz == klaz) {
				return known;
			}
		}

		return STRING;

	}

	public static boolean isValidType(final Class<?> klaz) {

		for (final PropertyType known : ENUM_VALS) {
			if (known.klaz == klaz) {
				return true;
			}
		}

		return false;

	}

	public static String getList() {
		final StringBuilder text = new StringBuilder(128);
		text.append(" ");
		for (final PropertyType known : ENUM_VALS) {
			text.append(known.value);
			text.append(" ");
		}
		return text.toString();
	}

}
