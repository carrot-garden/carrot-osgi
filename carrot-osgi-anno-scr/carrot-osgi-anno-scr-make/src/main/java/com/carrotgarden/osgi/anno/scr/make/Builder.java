/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.make;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.bean.AggregatorBean;
import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.bean.PropertyBean;
import com.carrotgarden.osgi.anno.scr.bean.PropertyFileBean;
import com.carrotgarden.osgi.anno.scr.bean.ProvideBean;
import com.carrotgarden.osgi.anno.scr.bean.ReferenceBean;
import com.carrotgarden.osgi.anno.scr.bean.ServiceBean;
import com.carrotgarden.osgi.anno.scr.conv.PropertyType;
import com.carrotgarden.osgi.anno.scr.util.Util;
import com.carrotgarden.osgi.anno.scr.util.UtilJdk;
import com.carrotgarden.osgi.anno.scr.util.UtilAsm;

public class Builder {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(Builder.class);

	/**
	 * names of interfaces that should be excluded from
	 * 
	 * <provide interface="..."/>
	 */
	private final Set<String> unwantedServiceSet;

	public Builder(final Set<String> unwantedServiceSet) {
		if (unwantedServiceSet == null) {
			this.unwantedServiceSet = new HashSet<String>();
		} else {
			this.unwantedServiceSet = unwantedServiceSet;
		}

	}

	public AggregatorBean makeAggregator(final List<Class<?>> klazList)
			throws Exception {

		final AggregatorBean aggregator = new AggregatorBean();

		for (final Class<?> klaz : klazList) {
			aggregator.componentList.add(makeComponent(klaz));
		}

		return aggregator;

	}

	/**
	 * super class annotations are overridden by derived classes in the type
	 * hierarchy
	 * 
	 * @throws IOException
	 */
	private ComponentBean makeComponent(final Class<?> klaz) throws Exception {

		final ComponentBean bean = new ComponentBean();

		final List<Class<?>> typeList = UtilJdk.inheritanceList(klaz);

		for (final Class<?> type : typeList) {

			final ClassNode node = UtilAsm.makeClassNode(type);

			applyServiceInferred(bean, type);

			applyPropertyEmbedded(bean, type, node);

			applyReference(bean, type, node);

			applyLifecycle(bean, type, node);

			// keep last
			applyComponent(bean, type, node);

		}

		finalizeProvidedServices(bean);

		return bean;

	}

	/**
	 * Collect component life cycle annotations.
	 */
	private void applyLifecycle(final ComponentBean bean, final Class<?> type,
			final ClassNode classNode) {

		int countActivate = 0;
		int countDeactivate = 0;
		int countModified = 0;

		@SuppressWarnings("unchecked")
		final List<MethodNode> mothodList = classNode.methods;

		for (final MethodNode methodNode : mothodList) {

			final String methodName = methodNode.name;

			@SuppressWarnings("unchecked")
			final List<AnnotationNode> annoList = methodNode.invisibleAnnotations;

			if (annoList == null || annoList.isEmpty()) {
				continue;
			}

			for (AnnotationNode annoNode : annoList) {

				final String annoDesc = annoNode.desc;

				if (UtilAsm.isActivateDesc(annoDesc)) {
					bean.activate = methodName;
					countActivate++;
				}

				if (UtilAsm.isDeactivateDesc(annoDesc)) {
					bean.deactivate = methodName;
					countDeactivate++;
				}

				if (UtilAsm.isModifiedDesc(annoDesc)) {
					bean.modified = methodName;
					countModified++;
				}

			}

		}

		if (countActivate > 1) {
			throw new IllegalStateException("duplicate @Activate in class : "
					+ type);
		}

		if (countDeactivate > 1) {
			throw new IllegalStateException("duplicate @Deactivate in class : "
					+ type);
		}

		if (countModified > 1) {
			throw new IllegalStateException("duplicate @Modified in class : "
					+ type);
		}

	}

	/**
	 * cleanup and discard unwanted service interfaces
	 */
	private void finalizeProvidedServices(final ComponentBean bean) {

		if (bean.service.provideSet.isEmpty()) {
			bean.service = null;
			return;
		}

		if (unwantedServiceSet.isEmpty()) {
			return;
		}

		final Set<ProvideBean> provideSet = new TreeSet<ProvideBean>();

		for (final ProvideBean provide : bean.service.provideSet) {
			if (unwantedServiceSet.contains(provide.type)) {
				continue; // ignore
			} else {
				provideSet.add(provide);
			}
		}

		if (provideSet.isEmpty()) {
			bean.service = null;
		} else {
			bean.service.provideSet = provideSet;
		}

	}

	private void applyComponent(final ComponentBean component,
			final Class<?> type, final ClassNode classNode) throws Exception {

		final AnnotationNode annoNode = UtilAsm.componentAnno(classNode);

		if (annoNode == null) {
			return;
		}

		//

		final String name = UtilAsm.asString(annoNode, "name");
		if (name != null) {
			component.name = name;
		} else {
			component.name = type.getName();
		}

		final Boolean enabled = UtilAsm.asBoolean(annoNode, "enabled");
		if (enabled != null) {
			component.enabled = enabled;
		}

		final String factory = UtilAsm.asString(annoNode, "factory");
		if (factory != null) {
			component.factory = factory;
		}

		final Boolean immediate = UtilAsm.asBoolean(annoNode, "immediate");
		if (immediate != null) {
			component.immediate = immediate;
		}

		final ConfigurationPolicy policy = UtilAsm.asEnum(annoNode,
				"configurationPolicy");
		if (policy != null) {
			component.configPolicy = policy;
		}

		//

		component.implementation.klaz = type.getName();

		final Boolean servicefactory = UtilAsm.asBoolean(annoNode,
				"servicefactory");
		if (servicefactory != null) {
			component.service.servicefactory = servicefactory;
		}

		//

		applyPropertyKeyValue(component, annoNode, type);

		applyPropertyFileEntry(component, annoNode, type);

		//

		applyServiceDeclared(component, annoNode, type);

	}

	/**
	 * Collect {@link Component#property()} entries.
	 */
	private void applyPropertyKeyValue(final ComponentBean bean,
			final AnnotationNode annoNode, final Class<?> klaz) {

		final List<String> entryList = UtilAsm.asStringList(annoNode,
				"property");

		if (entryList == null || entryList.isEmpty()) {
			return;
		}

		final String annoDesc = annoNode.desc;

		for (String entry : entryList) {

			if (!Util.isValidText(entry)) {
				throw new IllegalStateException("property must not be empty : "
						+ klaz + " / " + annoDesc);
			}

			if (!entry.contains("=")) {
				throw new IllegalStateException("property must contain '=' : "
						+ klaz + " / " + annoDesc);
			}

			final int indexEquals = entry.indexOf("=");

			final String nameType = entry.substring(0, indexEquals);

			if (nameType.length() == 0) {
				throw new IllegalStateException(
						"property name must not be empty : " + klaz + " / "
								+ annoDesc);
			}

			final String name;
			final String type;

			if (nameType.contains(":")) {

				final int indexColon = nameType.indexOf(":");

				name = nameType.substring(0, indexColon);
				type = PropertyType.from(nameType.substring(indexColon + 1)).value;

				if (name.length() == 0) {
					throw new IllegalStateException(
							"property name must not be empty : " + klaz + " / "
									+ annoDesc);
				}

			} else {
				name = nameType;
				type = PropertyType.STRING.value;
			}

			final String value = entry.substring(indexEquals + 1);

			final PropertyBean propBean = new PropertyBean();

			propBean.name = name;
			propBean.type = type;
			propBean.value = value;

			/** no duplicates */
			bean.propertySet.remove(propBean);
			bean.propertySet.add(propBean);

		}

	}

	/**
	 * Collect {@link Component#properties()} entries.
	 */
	private void applyPropertyFileEntry(final ComponentBean bean,
			final AnnotationNode annoNode, final Class<?> type) {

		final List<String> entryList = UtilAsm.asStringList(annoNode,
				"properties");

		if (entryList == null || entryList.isEmpty()) {
			return;
		}

		final String annoDesc = annoNode.desc;

		for (final String entry : entryList) {

			if (!Util.isValidText(entry)) {
				throw new IllegalArgumentException(
						"property file entry must not be empty : " + type
								+ " / " + annoDesc);
			}

			final PropertyFileBean propBean = new PropertyFileBean();

			propBean.entry = entry;

			/** no duplicates */
			bean.propertyFileSet.remove(propBean);
			bean.propertyFileSet.add(propBean);

		}

	}

	/**
	 * Collect static final {@link Property} fields.
	 */
	private void applyPropertyEmbedded(final ComponentBean component,
			final Class<?> type, ClassNode node) throws Exception {

		@SuppressWarnings("unchecked")
		final List<FieldNode> fieldList = node.fields;

		if (fieldList == null || fieldList.isEmpty()) {
			return;
		}

		for (FieldNode fieldNode : fieldList) {

			final AnnotationNode annoNode = UtilAsm.propertyAnno(fieldNode);

			if (annoNode == null) {
				continue;
			}

			final String annoName = UtilAsm.asString(annoNode, "name");

			final String fieldName = fieldNode.name;

			final Field field = type.getDeclaredField(fieldName);

			field.setAccessible(true);

			final int modifiers = field.getModifiers();

			if (!Modifier.isStatic(modifiers)) {
				throw new IllegalArgumentException(
						"property field must be static : " + type + " / "
								+ fieldName);
			}

			if (!Modifier.isFinal(modifiers)) {
				throw new IllegalArgumentException(
						"property field must be final : " + type + " / "
								+ fieldName);
			}

			if (!PropertyType.isValidType(field.getType())) {
				throw new IllegalArgumentException(
						"property field type must be one of : ["
								+ PropertyType.getList() + "] " + type + " / "
								+ fieldName + " / " + field.getType());
			}

			//

			final String name = Util.isValidText(annoName) ? annoName
					: fieldName;

			final Object value;
			try {
				value = field.get(null);
			} catch (final Exception e) {
				throw new IllegalArgumentException(
						"property annotated value is invalid : " + type + " / "
								+ fieldName, e);
			}

			final PropertyBean bean = new PropertyBean();

			bean.name = name;
			bean.type = PropertyType.from(value.getClass()).value;
			bean.value = "" + value;

			/** override if any */
			component.propertySet.remove(bean);
			component.propertySet.add(bean);

		}

	}

	/**
	 * Override collected service interfaces with explicit
	 * {@link Component#service()} statement, if present.
	 * 
	 * @throws Exception
	 */
	private void applyServiceDeclared(final ComponentBean component,
			final AnnotationNode annoNode, final Class<?> type)
			throws Exception {

		final List<Class<?>> serviceList = UtilAsm.asClassList(annoNode,
				"service");

		if (serviceList == null || serviceList.isEmpty()) {
			return;
		}

		final Set<ProvideBean> provideSet = component.service.provideSet;

		/** Discard previously collected interfaces. */
		provideSet.clear();

		for (final Class<?> service : serviceList) {

			if (!service.isAssignableFrom(type)) {
				throw new IllegalArgumentException(
						"annotated service must also be implemented : "
								+ service + " / " + type);
			}

			final ProvideBean bean = new ProvideBean();

			bean.type = service.getName();

			provideSet.add(bean);

		}

	}

	/**
	 * Collect all implied service interfaces.
	 */
	private void applyServiceInferred(final ComponentBean component,
			final Class<?> type) {

		final Class<?>[] ifaceArray = type.getInterfaces();

		if (ifaceArray.length == 0) {
			return;
		}

		final ServiceBean service = component.service;

		for (final Class<?> iface : ifaceArray) {

			final ProvideBean bean = new ProvideBean();
			bean.type = iface.getName();

			/** no duplicates */
			if (service.provideSet.contains(bean)) {
				continue;
			}

			service.provideSet.add(bean);

		}

	}

	/**
	 * Collect bind methods.
	 * 
	 * @throws Exception
	 */
	private void applyReference(final ComponentBean component,
			final Class<?> type, ClassNode classNode) throws Exception {

		@SuppressWarnings("unchecked")
		final List<MethodNode> methodList = classNode.methods;

		if (methodList == null || methodList.isEmpty()) {
			return;
		}

		for (MethodNode methodNode : methodList) {

			final AnnotationNode annoNode = UtilAsm.referenceAnno(methodNode);

			if (annoNode == null) {
				continue;
			}

			if (!UtilAsm.isValidBindParam(methodNode)) {
				throw new IllegalStateException(
						"invalid parameters for reference : " + methodNode.desc);
			}

			final ReferenceBean bean = makeReference(type, methodNode, annoNode);

			final Set<ReferenceBean> referenceSet = component.referenceSet;

			if (referenceSet.contains(bean)) {
				throw new IllegalStateException("duplicate reference : "
						+ methodNode.desc);
			}

			referenceSet.add(bean);

		}

	}

	private ReferenceBean makeReference(final Class<?> type,
			final MethodNode bindMethod, final AnnotationNode annoNode)
			throws Exception {

		final ReferenceBean reference = new ReferenceBean();

		final String bindName = bindMethod.name;

		final String bindType = UtilAsm.bindParameter(bindMethod)
				.getClassName();

		//

		reference.type = bindType;

		final String target = UtilAsm.asString(annoNode, "target");
		reference.target = Util.isValidText(target) ? target : null;

		/**
		 * Name must be unique in a component.
		 * <p>
		 * Use (interface type)/(target filter) combination.
		 */
		final String name = UtilAsm.asString(annoNode, "name");
		final String nameType = bindType;
		final String nameTarget = Util.isValidText(target) ? target : "*";
		final String nameDefault = nameType + "/" + nameTarget;
		reference.name = Util.isValidText(name) ? name : nameDefault;

		final ReferenceCardinality cardinality = UtilAsm.asEnum(annoNode,
				"cardinality");
		if (cardinality != null) {
			reference.cardinality = cardinality;
		}

		final ReferencePolicy policy = UtilAsm.asEnum(annoNode, "policy");
		if (policy != null) {
			reference.policy = policy;
		}

		reference.bind = bindName;

		reference.unbind = Util.unbindName(bindName);

		//

		UtilJdk.assertBindUnbindMatch(type, bindType, reference.bind,
				reference.unbind);

		return reference;

	}

}
