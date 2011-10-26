package com.carrotgarden.osgi.anno.scr.bean;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import com.carrotgarden.osgi.anno.scr.conv.ConfigurationPolicyConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("component")
public class ComponentBean {

	@XStreamAsAttribute
	@XStreamAlias("name")
	public String name;

	@XStreamAsAttribute
	@XStreamAlias("enabled")
	public boolean enabled = true;

	@XStreamAsAttribute
	@XStreamAlias("factory")
	public String factory;

	@XStreamAsAttribute
	@XStreamAlias("immediate")
	public boolean immediate = false;

	@XStreamAsAttribute
	@XStreamAlias("configuration-policy")
	@XStreamConverter(ConfigurationPolicyConverter.class)
	public ConfigurationPolicy policy;

	//

	@XStreamAsAttribute
	@XStreamAlias("activate")
	public String activate;

	@XStreamAsAttribute
	@XStreamAlias("deactivate")
	public String deactivate;

	@XStreamAsAttribute
	@XStreamAlias("modified")
	public String modified;

	//

	public ImplementationBean implementation;

	public ServiceBean service;

	@XStreamImplicit(itemFieldName = "reference")
	public List<ReferenceBean> referenceList = new LinkedList<ReferenceBean>();

	//

	ImplementationBean loadImplementation() {
		if (implementation == null) {
			implementation = new ImplementationBean();
		}
		return implementation;
	}

	ServiceBean loadService() {
		if (service == null) {
			service = new ServiceBean();
		}
		return service;
	}

	//

	public void apply(final Class<?> klaz) {

		final Component component = klaz.getAnnotation(Component.class);

		if (component != null) {

			name = Util.isValid(component.name()) ? component.name() : klaz
					.getName();

			enabled = component.enabled();

			factory = Util.isValid(component.factory()) ? component.factory()
					: null;

			immediate = component.immediate();

			policy = component.configurationPolicy();

			//

			loadImplementation().klaz = klaz.getName();

			loadService().servicefactory = component.servicefactory();

		}

		if (Util.hasInterfaces(klaz)) {
			loadService().apply(klaz);
		}

		if (Util.hasReferences(klaz)) {

			final Method[] methodArray = klaz.getDeclaredMethods();

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

				final ReferenceBean bean = new ReferenceBean();
				bean.apply(klaz, method, anno);

				if (referenceList.contains(bean)) {
					throw new IllegalArgumentException("duplicate reference : "
							+ method);
				}

				referenceList.add(bean);

			}

		}

	}

}
