package com.carrotgarden.osgi.anno.scr.bean;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("service")
public class ServiceBean {

	@XStreamAsAttribute
	@XStreamAlias("servicefactory")
	public boolean servicefactory = false;

	@XStreamImplicit(itemFieldName = "provide")
	public List<ProvideBean> provideList = new LinkedList<ProvideBean>();

	//

	public void apply(final Class<?> klaz) {

		final Class<?>[] ifaceArray = klaz.getInterfaces();

		if (ifaceArray.length == 0) {
			return;
		}

		for (final Class<?> iface : ifaceArray) {

			final ProvideBean bean = new ProvideBean();
			bean.klaz = iface.getName();

			if (provideList.contains(bean)) {
				continue;
			}

			provideList.add(bean);

		}

	}

}
