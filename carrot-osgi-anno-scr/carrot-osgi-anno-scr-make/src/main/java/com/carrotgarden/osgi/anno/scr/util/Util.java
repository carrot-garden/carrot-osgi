package com.carrotgarden.osgi.anno.scr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

public class Util {

	public static boolean isValidText(final String text) {
		return text != null && text.length() > 0;
	}

	public static boolean hasComponentAnno(final Class<?> klaz) {
		return klaz.isAnnotationPresent(Component.class);
	}

	public static boolean hasPropertyAnno(final Class<?> klaz) {
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

	public static boolean hasReferenceAnno(final Class<?> klaz) {
		final Method[] methodArray = klaz.getDeclaredMethods();
		for (final Method method : methodArray) {
			if (method.isAnnotationPresent(Reference.class)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static boolean isValid(final Object[] array) {
		return array != null && array.length > 0;
	}

	public static boolean isValidBindParam(final Class<?>[] paramArray) {

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

	/** using first parameter only */
	public static Class<?> bindType(final Method bindMethod) {

		final Class<?>[] paramArary = bindMethod.getParameterTypes();

		return paramArary[0];

	}

	public static String getUnbindName(final String bindName) {

		if (bindName.startsWith("add")) {
			return bindName.replaceFirst("add", "remove");
		}

		return "un" + bindName;

	}

	/**
	 * make sure : 1) bind/unbind names follow osgi spec; 2) have exactly one
	 * argument of the same type
	 */
	public static void assertBindUnbindMatch(final Class<?> klaz,
			final Class<?> type, final String bindName, final String unbindName) {

		final Method[] methodArray = klaz.getDeclaredMethods();

		int bindCount = 0;
		int unbindCount = 0;

		Method methodBind = null;
		Method methodUnbind = null;

		for (final Method method : methodArray) {

			final Class<?>[] paramArray = method.getParameterTypes();

			final boolean isParamMatch = paramArray.length == 1
					&& paramArray[0] == type;

			if (isParamMatch) {

				if (method.getName().equals(bindName)) {
					methodBind = method;
					bindCount++;
				}

				if (method.getName().equals(unbindName)) {
					methodUnbind = method;
					unbindCount++;
				}

			}

		}

		if (bindCount == 1 && unbindCount == 1) {
			return;
		}

		throw new IllegalArgumentException("mismatch : " + //
				" klaz=" + klaz.getName() + //
				" type=" + type.getName() + //
				" bind=" + methodBind + //
				" unbind=" + methodUnbind + //
				"");

	}

	/**
	 * ordered from most deep super class upwards
	 */
	public static List<Class<?>> getInheritanceList(final Class<?> klaz) {

		final LinkedList<Class<?>> list = new LinkedList<Class<?>>();

		Class<?> type = klaz;

		while (true) {

			if (type == null) {
				return list;
			}

			list.addFirst(type);

			type = type.getSuperclass();

		}

	}

	@SuppressWarnings("unused")
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

	public static String getMethodName(
			final Class<? extends Annotation> annoType, final Class<?> klazType) {

		final Method[] methodArray = klazType.getDeclaredMethods();

		for (final Method method : methodArray) {

			final Annotation anno = method.getAnnotation(annoType);

			if (anno != null) {
				return method.getName();
			}

		}

		return null;

	}

}
