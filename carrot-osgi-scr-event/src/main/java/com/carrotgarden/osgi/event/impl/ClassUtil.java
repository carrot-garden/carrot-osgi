/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.event.impl;


public class ClassUtil {

	public static class ClassTrace extends SecurityManager {

		public Class<?> getClassAt(final int index) {
			return getClassContext()[index];
		}

		public String getNameAt(final int index) {
			return getClassContext()[index].getName();
		}

		@Override
		public Class<?>[] getClassContext() {
			return super.getClassContext();
		}

		public String log(final int depth) {
			final Class<?>[] array = getClassContext();
			final int size = Math.min(array.length, depth);
			final StringBuilder text = new StringBuilder(128);
			for (int k = 0; k < size; k++) {
				text.append("stack : ");
				text.append(k);
				text.append(" = ");
				text.append(array[k].getName());
			}
			return text.toString();
		}

	}

}
