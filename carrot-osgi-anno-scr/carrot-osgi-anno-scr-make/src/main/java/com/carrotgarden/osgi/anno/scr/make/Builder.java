package com.carrotgarden.osgi.anno.scr.make;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

import com.carrotgarden.osgi.anno.scr.bean.AggregatorBean;
import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.bean.PropertyBean;
import com.carrotgarden.osgi.anno.scr.bean.PropertyFileBean;
import com.carrotgarden.osgi.anno.scr.bean.ProvideBean;
import com.carrotgarden.osgi.anno.scr.bean.ReferenceBean;
import com.carrotgarden.osgi.anno.scr.bean.ServiceBean;
import com.carrotgarden.osgi.anno.scr.conv.PropertyType;
import com.carrotgarden.osgi.anno.scr.util.Util;

public class Builder {

	/**
	 * names of interfaces that should be excluded from
	 * 
	 * <provide interface="..."/>
	 */
	private final Set<String> unwantedServiceSet;

	public Builder(final Set<String> unwantedServiceSet) {
		this.unwantedServiceSet = unwantedServiceSet;
	}

	public AggregatorBean makeAggregator(final List<Class<?>> klazList) {

		final AggregatorBean aggregator = new AggregatorBean();

		for (final Class<?> klaz : klazList) {
			aggregator.componentList.add(makeComponent(klaz));
		}

		return aggregator;

	}

	/**
	 * super class annotations are overridden by derived classes in the type
	 * hierarchy
	 */
	private ComponentBean makeComponent(final Class<?> klaz) {

		final ComponentBean bean = new ComponentBean();

		final List<Class<?>> typeList = Util.getInheritanceList(klaz);

		for (final Class<?> type : typeList) {

			applyServiceInferred(bean, type);

			applyPropertyEmbedded(bean, type);

			applyReference(bean, type);

			applyLifecycle(bean, type);

			// keep last
			applyComponent(bean, type);

		}

		finalizeProvidedServices(bean);

		return bean;

	}

	/**
	 * collect life cycle annotations
	 */
	private void applyLifecycle(final ComponentBean bean, final Class<?> type) {

		final Method[] methodArray = type.getDeclaredMethods();

		int countActivate = 0;
		int countDeactivate = 0;
		int countModified = 0;

		for (final Method method : methodArray) {

			final String methodName = method.getName();

			if (method.getAnnotation(Activate.class) != null) {
				bean.activate = methodName;
				countActivate++;
			}

			if (method.getAnnotation(Deactivate.class) != null) {
				bean.deactivate = methodName;
				countDeactivate++;
			}

			if (method.getAnnotation(Modified.class) != null) {
				bean.modified = methodName;
				countModified++;
			}

		}

		if (countActivate > 1) {
			throw new IllegalArgumentException(
					"duplicate @Activate in class : " + type);
		}

		if (countDeactivate > 1) {
			throw new IllegalArgumentException(
					"duplicate @Deactivate in class : " + type);
		}

		if (countModified > 1) {
			throw new IllegalArgumentException(
					"duplicate @Modified in class : " + type);
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
			final Class<?> type) {

		if (Util.hasComponentAnno(type)) {

			final Component anno = type.getAnnotation(Component.class);

			//

			final String name = anno.name();
			final String nameDefault = type.getName();
			component.name = Util.isValidText(name) ? name : nameDefault;

			component.enabled = anno.enabled();

			final String factory = anno.factory();
			component.factory = Util.isValidText(factory) ? factory : null;

			component.immediate = anno.immediate();

			component.policy = anno.configurationPolicy();

			//

			component.implementation.klaz = type.getName();

			component.service.servicefactory = anno.servicefactory();

			//

			applyPropertyKeyValue(component, anno, type);

			applyPropertyFileEntry(component, anno, type);

			//

			applyServiceDeclared(component, anno, type);

		}

	}

	private void applyPropertyKeyValue(final ComponentBean bean,
			final Component anno, final Class<?> klaz) {

		for (final String entry : anno.property()) {

			if (!Util.isValidText(entry)) {
				throw new IllegalArgumentException(
						"property must not be empty : " + klaz + " / " + anno);
			}

			if (!entry.contains("=")) {
				throw new IllegalArgumentException(
						"property must contain '=' : " + klaz + " / " + anno);
			}

			final int indexEquals = entry.indexOf("=");

			final String nameType = entry.substring(0, indexEquals);

			if (nameType.length() == 0) {
				throw new IllegalArgumentException(
						"property name must not be empty : " + klaz + " / "
								+ anno);
			}

			final String name;
			final String type;

			if (nameType.contains(":")) {

				final int indexColon = nameType.indexOf(":");

				name = nameType.substring(0, indexColon);
				type = PropertyType.from(nameType.substring(indexColon + 1)).value;

				if (name.length() == 0) {
					throw new IllegalArgumentException(
							"property name must not be empty : " + klaz + " / "
									+ anno);
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

	private void applyPropertyFileEntry(final ComponentBean bean,
			final Component anno, final Class<?> type) {

		for (final String entry : anno.properties()) {

			if (!Util.isValidText(entry)) {
				throw new IllegalArgumentException(
						"property file entry must not be empty : " + type
								+ " / " + anno);
			}

			final PropertyFileBean propBean = new PropertyFileBean();

			propBean.entry = entry;

			/** no duplicates */
			bean.propertyFileSet.remove(propBean);
			bean.propertyFileSet.add(propBean);

		}

	}

	/**
	 * collect static final annotated property fields
	 */
	private void applyPropertyEmbedded(final ComponentBean component,
			final Class<?> type) {

		if (!Util.hasPropertyAnno(type)) {
			return;
		}

		final Field[] fieldArray = type.getDeclaredFields();

		for (final Field field : fieldArray) {

			final Property anno = field.getAnnotation(Property.class);

			if (anno == null) {
				continue;
			}

			field.setAccessible(true);

			final String fieldName = field.getName();

			final int modifiers = field.getModifiers();

			if (!Modifier.isStatic(modifiers)) {
				throw new IllegalArgumentException(
						"property field must be static : " + fieldName);
			}

			if (!Modifier.isFinal(modifiers)) {
				throw new IllegalArgumentException(
						"property field must be final : " + fieldName);
			}

			if (!String.class.equals(field.getType())) {
				throw new IllegalArgumentException(
						"property field must be java.lang.String : "
								+ fieldName + " / " + field.getType());
			}

			//

			final String name = Util.isValidText(anno.name()) ? anno.name()
					: fieldName;

			final String value;
			try {
				value = (String) field.get(null);
			} catch (final Exception e) {
				throw new IllegalArgumentException(
						"property annotated value is invalid : " + fieldName, e);
			}

			final PropertyBean bean = new PropertyBean();

			bean.name = name;
			bean.type = PropertyType.STRING.value;
			bean.value = value;

			/** override if any */
			component.propertySet.remove(bean);
			component.propertySet.add(bean);

		}

	}

	/**
	 * override collected service interfaces with explicit "service=..." if
	 * present
	 * */
	private void applyServiceDeclared(final ComponentBean component,
			final Component anno, final Class<?> type) {

		final Class<?>[] serviceArray = anno.service();

		if (serviceArray == null || serviceArray.length == 0) {
			return;
		}

		final Set<ProvideBean> provideSet = component.service.provideSet;

		/** discard previously collected interfaces */
		provideSet.clear();

		for (final Class<?> service : serviceArray) {

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
	 * collect all service interfaces
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
	 * collect bind methods
	 */
	private void applyReference(final ComponentBean component,
			final Class<?> type) {

		final Method[] methodArray = type.getDeclaredMethods();

		for (final Method method : methodArray) {

			final Reference anno = method.getAnnotation(Reference.class);

			if (anno == null) {
				continue;
			}

			final Class<?>[] paramArray = method.getParameterTypes();

			if (!Util.isValidBindParam(paramArray)) {
				throw new IllegalArgumentException("invalid reference : "
						+ method);
			}

			final ReferenceBean bean = makeReference(type, method, anno);

			final Set<ReferenceBean> referenceSet = component.referenceSet;

			if (referenceSet.contains(bean)) {
				throw new IllegalArgumentException("duplicate reference : "
						+ method);
			}

			referenceSet.add(bean);

		}

	}

	private ReferenceBean makeReference(final Class<?> type,
			final Method bindMethod, final Reference anno) {

		final ReferenceBean reference = new ReferenceBean();

		final String bindName = bindMethod.getName();

		final Class<?> bindType = Util.bindType(bindMethod);

		//

		reference.type = bindType.getName();

		final String target = anno.target();
		reference.target = Util.isValidText(target) ? target : null;

		/**
		 * name must be unique in a component;
		 * 
		 * use (interface type)/(target filter) combination
		 */
		final String name = anno.name();
		final String nameType = bindType.getName();
		final String nameTarget = Util.isValidText(target) ? target : "*";
		final String nameDefault = nameType + "/" + nameTarget;
		reference.name = Util.isValidText(name) ? name : nameDefault;

		reference.cardinality = anno.cardinality();

		reference.policy = anno.policy();

		reference.bind = bindName;

		reference.unbind = Util.unbindName(bindName);

		//

		Util.assertBindUnbindMatch(type, bindType, reference.bind,
				reference.unbind);

		return reference;

	}

}
