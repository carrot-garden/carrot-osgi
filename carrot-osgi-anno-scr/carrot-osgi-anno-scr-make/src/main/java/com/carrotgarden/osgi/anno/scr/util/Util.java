/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic DS utilities.
 */
public class Util {

	/**
	 * Ensure zero count.
	 */
	public static void assertZeroCount(int count, String message) {
		if (count == 0) {
			return;
		}
		throw new IllegalStateException(message);
	}

	/**
	 * Concatenate lists that permit null.
	 */
	public static <T> List<T> concatenate(List<T> one, List<T> two) {
		if (one == null && two == null) {
			return null;
		}
		if (one != null && two == null) {
			return one;
		}
		if (one == null && two != null) {
			return two;
		}
		final int size = one.size() + two.size();
		final List<T> list = new ArrayList<T>(size);
		list.addAll(one);
		list.addAll(two);
		return list;
	}

	/**
	 * Check for null or empty list.
	 */
	public static boolean isListNone(List<?> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * Check for null or empty text.
	 */
	public static boolean isValidText(final String text) {
		return text != null && text.length() > 0;
	}

	/**
	 * Generate unbind method name per OSGI spec.
	 */
	public static String unbindName(final String bindName) {

		if (bindName.startsWith("add")) {
			return bindName.replaceFirst("add", "remove");
		}

		return "un" + bindName;

	}

	private Util() {

	}

}
