/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.osgi.service.component.annotations.*;

/**
 * DS utilities which rely on ASM reflection.
 */
public class UtilAsm {

	public static final String DESC_CONFIGURATION_POLICY = Type
			.getDescriptor(ConfigurationPolicy.class);

	public static final String DESC_COMPONENT = Type
			.getDescriptor(Component.class);

	public static final String DESC_ACTIVATE = Type
			.getDescriptor(Activate.class);

	public static final String DESC_DEACTIVATE = Type
			.getDescriptor(Deactivate.class);

	public static final String DESC_MODIFIED = Type
			.getDescriptor(Modified.class);

	public static final String DESC_PROPERTY = Type
			.getDescriptor(Property.class);

	public static final String DESC_REFERENCE = Type
			.getDescriptor(Reference.class);

	public static final String DESC_REFERENCE_CARDINALITY = Type
			.getDescriptor(ReferenceCardinality.class);

	public static final String DESC_REFERENCE_POLICY = Type
			.getDescriptor(ReferencePolicy.class);

	public static final String DESC_REFERENCE_POLICY_OPTION = Type
			.getDescriptor(ReferencePolicyOption.class);

	public static final int SKIP_MODE = ClassReader.SKIP_CODE
			| ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;

	/**
	 * Extract annotation value as {@link Boolean}.
	 */
	public static Boolean asBoolean(AnnotationNode node, String name) {

		@SuppressWarnings("unchecked")
		final List<Object> entryList = node.values;

		if (Util.isListNone(entryList)) {
			return null;
		}

		for (int k = 0; k < entryList.size(); k += 2) {
			final String entryName = (String) entryList.get(k);
			if (entryName.equals(name)) {
				final Boolean entryValue = (Boolean) entryList.get(k + 1);
				return entryValue;
			}
		}

		return null;

	}

	/**
	 * Extract annotation value as class list.
	 */
	public static List<Class<?>> asClassList(AnnotationNode node, String name)
			throws Exception {

		@SuppressWarnings("unchecked")
		final List<Object> entryList = node.values;

		if (Util.isListNone(entryList)) {
			return null;
		}

		for (int k = 0; k < entryList.size(); k += 2) {
			final String entryName = (String) entryList.get(k);
			if (entryName.equals(name)) {

				final ClassLoader loader = UtilAsm.class.getClassLoader();
				final List<Class<?>> klazList = new ArrayList<Class<?>>();

				@SuppressWarnings("unchecked")
				final List<Type> entryValue = (List<Type>) entryList.get(k + 1);

				for (Type type : entryValue) {
					final Class<?> klaz = loader.loadClass(type.getClassName());
					klazList.add(klaz);
				}

				return klazList;
			}
		}

		return null;

	}

	/**
	 * Extract annotation value as type safe enum.
	 */
	public static <E extends Enum<E>> E asEnum(AnnotationNode node, String name)
			throws Exception {

		@SuppressWarnings("unchecked")
		final List<Object> entryList = node.values;

		if (Util.isListNone(entryList)) {
			return null;
		}

		for (int k = 0; k < entryList.size(); k += 2) {

			final String entryName = (String) entryList.get(k);

			if (entryName.equals(name)) {

				final String[] entryValue = (String[]) entryList.get(k + 1);
				final String desc = entryValue[0];
				final String value = entryValue[1];

				final String klazName = Type.getType(desc).getClassName();

				@SuppressWarnings("unchecked")
				final Class<E> klaz = (Class<E>) UtilAsm.class.getClassLoader()
						.loadClass(klazName);

				for (E entry : klaz.getEnumConstants()) {
					if (entry.name().equals(value)) {
						return entry;
					}
				}

				throw new IllegalStateException("invalid enum klaz=" + klaz);

			}
		}

		return null;

	}

	/**
	 * Extract annotation value as a string.
	 */
	public static String asString(AnnotationNode node, String name) {

		@SuppressWarnings("unchecked")
		final List<Object> entryList = node.values;

		if (Util.isListNone(entryList)) {
			return null;
		}

		for (int k = 0; k < entryList.size(); k += 2) {
			final String entryName = (String) entryList.get(k);
			if (entryName.equals(name)) {
				final String entryValue = (String) entryList.get(k + 1);
				return entryValue;
			}
		}

		return null;

	}

	/**
	 * Extract annotation value as string list.
	 */
	public static List<String> asStringList(AnnotationNode node, String name) {

		@SuppressWarnings("unchecked")
		final List<Object> entryList = node.values;

		if (Util.isListNone(entryList)) {
			return null;
		}

		for (int k = 0; k < entryList.size(); k += 2) {
			final String entryName = (String) entryList.get(k);
			if (entryName.equals(name)) {
				@SuppressWarnings("unchecked")
				final List<String> entryValue = (List<String>) entryList
						.get(k + 1);
				return entryValue;
			}
		}

		return null;
	}

	/**
	 * Make ASM class node for a JDK class.
	 * <p>
	 * Use class's own class loader to locate byte codes.
	 */
	public static ClassNode classNode(final Class<?> klaz) throws Exception {

		final String name = klaz.getName();
		final String path = name.replace('.', '/') + ".class";

		ClassLoader loader = klaz.getClassLoader();
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}

		final InputStream stream = loader.getResourceAsStream(path);

		final ClassReader reader = new ClassReader(stream);

		final ClassNode node = new ClassNode();

		reader.accept(node, SKIP_MODE);

		return node;
	}

	/**
	 * Find component annotation on a class.
	 * 
	 * @return {@link Component} {@link AnnotationNode} or null if missing.
	 */
	public static AnnotationNode componentAnno(ClassNode node) {

		@SuppressWarnings("unchecked")
		final List<AnnotationNode> annoList = node.invisibleAnnotations;

		if (Util.isListNone(annoList)) {
			return null;
		}

		for (final AnnotationNode annoNode : annoList) {
			if (isComponentDesc(annoNode.desc)) {
				return annoNode;
			}
		}

		return null;

	}

	/**
	 * Extract first method parameter type.
	 */
	public static Type firstParamType(MethodNode node) {
		return parameterArray(node)[0];
	}

	/**
	 * Check component annotation on a class.
	 */
	public static boolean hasComponentAnno(Class<?> klaz) throws Exception {

		final ClassNode node = classNode(klaz);

		final AnnotationNode anno = componentAnno(node);

		return anno != null;

	}

	/**
	 * Check class if class descriptor is {@link Activate}
	 */
	public static boolean isActivateDesc(String desc) {
		return DESC_ACTIVATE.equals(desc);
	}

	/**
	 * Check class if class descriptor is {@link Component}
	 */
	public static boolean isComponentDesc(String desc) {
		return DESC_COMPONENT.equals(desc);
	}

	/**
	 * Check class if class descriptor is {@link Deactivate}
	 */
	public static boolean isDeactivateDesc(String desc) {
		return DESC_DEACTIVATE.equals(desc);
	}

	/**
	 * Check class if class descriptor is {@link Modified}
	 */
	public static boolean isModifiedDesc(String desc) {
		return DESC_MODIFIED.equals(desc);
	}

	/**
	 * Check class if class descriptor is {@link Property}
	 */
	public static boolean isPropertyDesc(String desc) {
		return DESC_PROPERTY.equals(desc);
	}

	/**
	 * Check class if class descriptor is {@link Reference}
	 */
	public static boolean isReferenceDesc(String desc) {
		return DESC_REFERENCE.equals(desc);
	}

	/**
	 * 112.3.1
	 * <p>
	 * When using the event strategy, the bind and unbind methods must have one
	 * of the following proto-types:
	 * <p>
	 * void <method-name>(ServiceReference);
	 * <p>
	 * void <method-name>(<parameter-type>);
	 * <p>
	 * void <method-name>(<parameter-type>, Map);
	 * <p>
	 * A suitable method is selected using the following priority:
	 * <p>
	 * 1. The method takes a single argument and the type of the argument is
	 * org.osgi.framework.ServiceReference.
	 * <p>
	 * 2. The method takes a single argument and the type of the argument is the
	 * type specified by the reference’s interface attribute.
	 * <p>
	 * 3. The method takes a single argument and the type of the argument is
	 * assignable from the type specified by the reference’s interface
	 * attribute. If multiple methods match this rule, this implies the method
	 * name is overloaded and SCR may choose any of the methods to call.
	 * <p>
	 * 4. The method takes two argument and the type of the first argument is
	 * the type specified by the reference’s interface attribute and the type of
	 * the second argument is java.util.Map.
	 * <p>
	 * 5. The method takes two argument and the type of the first argument is
	 * assignable from the type specified by the reference’s interface attribute
	 * and the type of the second argument is java.util.Map. If multiple methods
	 * match this rule, this implies the method name is overloaded and SCR may
	 * choose any of the methods to call.
	 */
	public static boolean isValidBindParam(MethodNode node) throws Exception {
		final ClassLoader loader = UtilAsm.class.getClassLoader();
		final Type[] array = parameterArray(node);
		switch (array.length) {
		case 1: {
			final Class<?> klaz1 = loader.loadClass(array[0].getClassName());
			return klaz1.isInterface();
		}
		case 2: {
			final Class<?> klaz1 = loader.loadClass(array[0].getClassName());
			final Class<?> klaz2 = loader.loadClass(array[1].getClassName());
			return klaz1.isInterface()
					&& java.util.Map.class.isAssignableFrom(klaz2);
		}
		default:
			return false;
		}
	}

	/**
	 * Extract method parameters.
	 */
	public static Type[] parameterArray(MethodNode node) {
		return Type.getMethodType(node.desc).getArgumentTypes();
	}

	/**
	 * Find property annotation on a field.
	 * 
	 * @return {@link Property} {@link AnnotationNode} or null if missing.
	 */
	public static AnnotationNode propertyAnno(FieldNode node) {

		@SuppressWarnings("unchecked")
		final List<AnnotationNode> annoList = node.invisibleAnnotations;

		if (Util.isListNone(annoList)) {
			return null;
		}

		for (final AnnotationNode annoNode : annoList) {
			if (isPropertyDesc(annoNode.desc)) {
				return annoNode;
			}
		}

		return null;

	}

	/**
	 * Find reference annotation on a method.
	 * 
	 * @return {@link Reference} {@link AnnotationNode} or null if missing.
	 */
	public static AnnotationNode referenceAnno(MethodNode node) {

		@SuppressWarnings("unchecked")
		final List<AnnotationNode> annoList = node.invisibleAnnotations;

		if (Util.isListNone(annoList)) {
			return null;
		}

		for (final AnnotationNode annoNode : annoList) {
			if (isReferenceDesc(annoNode.desc)) {
				return annoNode;
			}
		}

		return null;

	}

	private UtilAsm() {

	}

}
