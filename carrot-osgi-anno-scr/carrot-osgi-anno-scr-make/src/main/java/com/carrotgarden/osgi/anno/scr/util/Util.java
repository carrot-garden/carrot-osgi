/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

public class Util {

	private static final ConfigurationPolicy[] ENUM_ConfigurationPolicy = ConfigurationPolicy
			.values();

	private static final ReferenceCardinality[] ENUM_ReferenceCardinality = ReferenceCardinality
			.values();

	private static final ReferencePolicy[] ENUM_ReferencePolicy = ReferencePolicy
			.values();

	/**
	 * make sure : 1) bind/unbind names follow osgi spec; 2) have first argument
	 * of the same type; 3) optional second argument is java.util.Map
	 */
	public static void assertBindUnbindMatch(final Class<?> klaz,
			final Class<?> type, final String nameBind, final String nameUnbind) {

		final Method[] methodArray = klaz.getDeclaredMethods();

		int countBind = 0;
		int countUnbind = 0;

		Method methodBind = null;
		Method methodUnbind = null;

		for (final Method method : methodArray) {

			final Class<?>[] paramArray = method.getParameterTypes();

			final boolean isParamMatch;

			switch (paramArray.length) {
			default:
			case 0:
				continue;
			case 1:
				isParamMatch = paramArray[0] == type;
				break;
			case 2:
				isParamMatch = paramArray[0] == type
						&& java.util.Map.class.isAssignableFrom(paramArray[1]);
				break;
			}

			if (isParamMatch) {

				if (method.getName().equals(nameBind)) {
					methodBind = method;
					countBind++;
				}

				if (method.getName().equals(nameUnbind)) {
					methodUnbind = method;
					countUnbind++;
				}

			}

		}

		if (countBind == 1 && countUnbind == 1) {
			return;
		}

		throw new IllegalArgumentException("mismatch : " + //
				" klaz=" + klaz.getName() + //
				" type=" + type.getName() + //
				" bind=" + methodBind + //
				" unbind=" + methodUnbind + //
				"");

	}

	/** using first parameter only */
	public static Class<?> bindType(final Method bindMethod) {

		final Class<?>[] paramArary = bindMethod.getParameterTypes();

		return paramArary[0];

	}

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * OPTIONAL if not resolved
	 */
	public static ConfigurationPolicy fromConfigurationPolicy(final String value) {

		for (final ConfigurationPolicy known : ENUM_ConfigurationPolicy) {
			if (known.toString().equalsIgnoreCase(value)) {
				return known;
			}
		}

		return ConfigurationPolicy.OPTIONAL;

	}

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * MANDATORY if not resolved
	 */
	public static ReferenceCardinality fromReferenceCardinality(
			final String value) {

		for (final ReferenceCardinality known : ENUM_ReferenceCardinality) {
			if (known.toString().equalsIgnoreCase(value)) {
				return known;
			}
		}

		return ReferenceCardinality.MANDATORY;

	}

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * STATIC if not resolved
	 */
	public static ReferencePolicy fromReferencePolicy(final String value) {

		for (final ReferencePolicy known : ENUM_ReferencePolicy) {
			if (known.toString().equalsIgnoreCase(value)) {
				return known;
			}
		}

		return ReferencePolicy.STATIC;

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

	public static String getUnbindName(final String bindName) {

		if (bindName.startsWith("add")) {
			return bindName.replaceFirst("add", "remove");
		}

		return "un" + bindName;

	}

	public static boolean hasComponentAnno(final Class<?> klaz) {
		return klaz.isAnnotationPresent(Component.class);
	}

	public static boolean hasInterfaces(final Class<?> klaz) {
		return klaz.getInterfaces().length > 0;
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

	public static boolean hasReferenceAnno(final Class<?> klaz) {
		final Method[] methodArray = klaz.getDeclaredMethods();
		for (final Method method : methodArray) {
			if (method.isAnnotationPresent(Reference.class)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAbstract(final Class<?> klaz) {
		return Modifier.isAbstract(klaz.getModifiers());
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

	@SuppressWarnings("unused")
	private static boolean isValid(final Object[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 112.3.1
	 * 
	 * When using the event strategy, the bind and unbind methods must have one
	 * of the following proto-types:
	 * 
	 * void <method-name>(ServiceReference);
	 * 
	 * void <method-name>(<parameter-type>);
	 * 
	 * void <method-name>(<parameter-type>, Map);
	 * 
	 * A suitable method is selected using the following priority:
	 * 
	 * 1. The method takes a single argument and the type of the argument is
	 * org.osgi.framework.ServiceReference.
	 * 
	 * 2. The method takes a single argument and the type of the argument is the
	 * type specified by the reference’s interface attribute.
	 * 
	 * 3. The method takes a single argument and the type of the argument is
	 * assignable from the type specified by the reference’s interface
	 * attribute. If multiple methods match this rule, this implies the method
	 * name is overloaded and SCR may choose any of the methods to call.
	 * 
	 * 4. The method takes two argument and the type of the first argument is
	 * the type specified by the reference’s interface attribute and the type of
	 * the second argument is java.util.Map.
	 * 
	 * 5. The method takes two argument and the type of the first argument is
	 * assignable from the type specified by the reference’s interface attribute
	 * and the type of the second argument is java.util.Map. If multiple methods
	 * match this rule, this implies the method name is overloaded and SCR may
	 * choose any of the methods to call.
	 */
	public static boolean isValidBindParam(final Class<?>[] paramArray) {

		if (paramArray == null) {
			return false;
		}

		switch (paramArray.length) {

		case 0:
			return false;

		case 1:
			if (paramArray[0].isInterface()) {
				return true;
			} else {
				return false;
			}

		case 2:
			if (paramArray[0].isInterface()
					&& java.util.Map.class.isAssignableFrom(paramArray[1])) {
				return true;
			} else {
				return false;
			}

		default:
			return false;
		}

	}

	public static boolean isValidText(final String text) {
		return text != null && text.length() > 0;
	}

	private Util() {

	}

}
