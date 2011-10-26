package com.carrotgarden.osgi.anno.scr.bean;

import java.lang.reflect.Method;

import org.osgi.service.component.annotations.Reference;

class Util {

	static boolean isValid(final String text) {
		return text != null && text.length() > 0;
	}

	static boolean hasInterfaces(final Class<?> klaz) {
		return klaz.getInterfaces().length > 0;
	}

	static boolean hasReferences(final Class<?> klaz) {
		final Method[] methodArray = klaz.getDeclaredMethods();
		for (final Method method : methodArray) {
			if (method.isAnnotationPresent(Reference.class)) {
				return true;
			}
		}
		return false;
	}

	static boolean isValid(final Class<?>[] paramArray) {
		return paramArray != null && paramArray.length == 1;
	}

}
