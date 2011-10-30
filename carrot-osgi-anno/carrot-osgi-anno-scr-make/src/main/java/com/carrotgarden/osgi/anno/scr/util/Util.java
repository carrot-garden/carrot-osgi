package com.carrotgarden.osgi.anno.scr.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

public class Util {

	public static boolean isValid(final String text) {
		return text != null && text.length() > 0;
	}

	public static boolean hasComponent(final Class<?> klaz) {
		return klaz.isAnnotationPresent(Component.class);
	}

	public static boolean hasPropAnnotation(final Class<?> klaz) {
		final Field[] fieldArray = klaz.getDeclaredFields();
		for (final Field field : fieldArray) {
			if (field.isAnnotationPresent(Property.class)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasInterfaces(final Class<?> klaz) {
		return klaz.getInterfaces().length > 0;
	}

	public static boolean hasReferences(final Class<?> klaz) {
		final Method[] methodArray = klaz.getDeclaredMethods();
		for (final Method method : methodArray) {
			if (method.isAnnotationPresent(Reference.class)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValid(final Object[] array) {
		return array != null && array.length > 0;
	}

	public static boolean isValid(final Class<?>[] paramArray) {

		if (paramArray == null) {
			return false;
		}

		if (paramArray.length != 1) {
			return false;
		}

		final Class<?> type = paramArray[0];

		if (!type.isInterface()) {
			return false;
		}

		return true;
	}

	public static Class<?> bindType(final Method bindMethod) {

		final Class<?>[] paramArary = bindMethod.getParameterTypes();

		return paramArary[0];

	}

	public static String unbindName(final String bindName) {

		if (bindName.startsWith("add")) {
			return "remove" + bindName.replaceFirst("add", "");
		}

		return "un" + bindName;

	}

	public static void assertBindUnbind(final Class<?> klaz,
			final Class<?> type, final String bindName, final String unbindName) {

		final Method[] methodArray = klaz.getDeclaredMethods();

		int bindCount = 0;
		int unbindCount = 0;

		for (final Method method : methodArray) {

			final Class<?>[] paramArray = method.getParameterTypes();

			if (method.getName().equals(bindName) && paramArray.length == 1
					&& paramArray[0] == type) {
				bindCount++;
			}

			if (method.getName().equals(unbindName) && paramArray.length == 1
					&& paramArray[0] == type) {
				unbindCount++;
			}

		}

		if (bindCount == 1 && unbindCount == 1) {
			return;
		}

		throw new RuntimeException("mismatch : " + //
				" klaz=" + klaz.getName() + //
				" type=" + type.getName() + //
				" bind=" + bindName + //
				" unbind=" + unbindName + //
				"");

	}

	public static List<Class<?>> getInheritanceList(final Class<?> klaz) {

		final List<Class<?>> list = new LinkedList<Class<?>>();

		Class<?> type = klaz;

		while (true) {

			if (type == null) {
				return list;
			}

			list.add(0, type);

			type = type.getSuperclass();

		}

	}

	private static boolean isComponent(final Class<?> klaz) {

		if (klaz == null) {
			return false;
		}

		if (klaz.isAnnotationPresent(Component.class)) {
			return true;
		} else {
			return isComponent(klaz.getSuperclass());
		}

	}

	public static boolean isAbstract(final Class<?> klaz) {
		return Modifier.isAbstract(klaz.getModifiers());
	}

}