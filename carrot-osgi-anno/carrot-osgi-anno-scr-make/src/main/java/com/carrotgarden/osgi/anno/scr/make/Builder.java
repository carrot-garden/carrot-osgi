package com.carrotgarden.osgi.anno.scr.make;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

import com.carrotgarden.osgi.anno.scr.bean.AggregatorBean;
import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.bean.PropertyBean;
import com.carrotgarden.osgi.anno.scr.bean.ProvideBean;
import com.carrotgarden.osgi.anno.scr.bean.ReferenceBean;
import com.carrotgarden.osgi.anno.scr.bean.ServiceBean;
import com.carrotgarden.osgi.anno.scr.util.Util;

public class Builder {

	private final Set<String> ignoreService;

	public Builder(final Set<String> ignoreService) {
		this.ignoreService = ignoreService;
	}

	public AggregatorBean makeAggregator(final List<Class<?>> klazList) {

		final AggregatorBean aggregator = new AggregatorBean();

		for (final Class<?> klaz : klazList) {
			aggregator.componentList.add(makeComponent(klaz));
		}

		return aggregator;

	}

	private ComponentBean makeComponent(final Class<?> klaz) {

		final ComponentBean bean = new ComponentBean();

		final List<Class<?>> typeList = Util.getInheritanceList(klaz);

		for (final Class<?> type : typeList) {

			applyComponent(bean, type);

			applyService(bean.service, type);

			applyProperty(bean.propertyList, type);

			applyReference(bean.referenceList, type);

		}

		filterService(bean);

		return bean;

	}

	private void filterService(final ComponentBean bean) {

		if (bean.service.provideList.isEmpty()) {
			bean.service = null;
			return;
		}

		if (ignoreService.isEmpty()) {
			return;
		}

		final List<ProvideBean> filteredList = new LinkedList<ProvideBean>();

		for (final ProvideBean provide : bean.service.provideList) {
			if (ignoreService.contains(provide.type)) {
				continue;
			} else {
				filteredList.add(provide);
			}
		}

		if (filteredList.isEmpty()) {
			bean.service = null;
		} else {
			bean.service.provideList = filteredList;
		}

	}

	private void applyComponent(final ComponentBean bean, final Class<?> type) {

		if (Util.hasComponent(type)) {

			final Component anno = type.getAnnotation(Component.class);

			bean.name = Util.isValid(anno.name()) ? anno.name() : type
					.getName();

			bean.enabled = anno.enabled();

			bean.factory = Util.isValid(anno.factory()) ? anno.factory() : null;

			bean.immediate = anno.immediate();

			bean.policy = anno.configurationPolicy();

			//

			bean.implementation.klaz = type.getName();

			//

			bean.service.servicefactory = anno.servicefactory();

		}

	}

	private void applyProperty(final List<PropertyBean> propertyList,
			final Class<?> type) {

		if (!Util.hasProperties(type)) {
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
						"property annotated type is invalid : " + fieldName
								+ " / " + field.getType());
			}

			//

			final String name = Util.isValid(anno.name()) ? anno.name()
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
			bean.value = value;

			propertyList.add(bean);

		}

	}

	private void applyService(final ServiceBean service, final Class<?> type) {

		final Class<?>[] ifaceArray = type.getInterfaces();

		if (ifaceArray.length == 0) {
			return;
		}

		for (final Class<?> iface : ifaceArray) {

			final ProvideBean bean = new ProvideBean();
			bean.type = iface.getName();

			if (service.provideList.contains(bean)) {
				continue;
			}

			service.provideList.add(bean);

		}

	}

	private void applyReference(final List<ReferenceBean> referenceList,
			final Class<?> type) {

		final Method[] methodArray = type.getDeclaredMethods();

		for (final Method method : methodArray) {

			final Reference anno = method.getAnnotation(Reference.class);

			if (anno == null) {
				continue;
			}

			final Class<?>[] paramArray = method.getParameterTypes();

			if (!Util.isValid(paramArray)) {
				throw new IllegalArgumentException("invalid reference : "
						+ method);
			}

			final ReferenceBean bean = makeReference(type, method, anno);

			if (referenceList.contains(bean)) {
				throw new IllegalArgumentException("duplicate reference : "
						+ method);
			}

			referenceList.add(bean);

		}

	}

	private ReferenceBean makeReference(final Class<?> type,
			final Method bindMethod, final Reference anno) {

		final ReferenceBean bean = new ReferenceBean();

		final String bindName = bindMethod.getName();

		final Class<?> bindType = Util.bindType(bindMethod);

		//

		bean.type = bindType.getName();

		bean.target = Util.isValid(anno.target()) ? anno.target() : null;

		bean.name = Util.isValid(anno.name()) ? anno.name() : bean.type + "/"
				+ bean.target;

		bean.cardinality = anno.cardinality();

		bean.policy = anno.policy();

		bean.bind = bindName;

		bean.unbind = Util.unbindName(bindName);

		//

		Util.assertBindUnbind(type, bindType, bean.bind, bean.unbind);

		return bean;

	}

}
