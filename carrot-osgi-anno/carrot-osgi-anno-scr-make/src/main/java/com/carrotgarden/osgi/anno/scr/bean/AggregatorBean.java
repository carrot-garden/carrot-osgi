package com.carrotgarden.osgi.anno.scr.bean;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("container")
public class AggregatorBean {

	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	public String xmlns = "http://www.osgi.org/xmlns/scr/v1.1.0";

	@XStreamImplicit(itemFieldName = "component")
	public List<ComponentBean> componentList = new LinkedList<ComponentBean>();

	//

	public void apply(final Class<?>... klazArray) {

		for (final Class<?> klaz : klazArray) {

			if (!Util.isAnnotationPresent(klaz)) {
				continue;
			}

			final ComponentBean bean = new ComponentBean();

			final List<Class<?>> list = Util.getClassList(klaz);

			for (final Class<?> type : list) {

				bean.apply(type);

			}

			componentList.add(bean);

		}

	}

}
