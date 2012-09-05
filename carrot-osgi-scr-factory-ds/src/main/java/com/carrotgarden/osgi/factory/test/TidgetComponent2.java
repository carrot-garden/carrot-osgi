/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.factory.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.factory.api.FactoryDescriptor;

/** factory component */
@Component(factory = TidgetComponent2.FACTORY)
public class TidgetComponent2 implements Tidget {

	@FactoryDescriptor
	private static final Map<String, String> descriptor;
	static {
		descriptor = new HashMap<String, String>();
		descriptor.put("name", "tester-factory-2");
	}

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static final String FACTORY = "f8719678-3aa6-4705-9cec-9fbdc32c7cbf";

	protected String id = UUID.randomUUID().toString();

	private Map<String, String> props;

	@Override
	public String getInstanceId() {
		return id;
	}

	@Activate
	protected void activate(final Map<String, String> props) {

		this.props = props;

	}

	@Deactivate
	protected void dectivate() {

	}

	@Override
	public Map<String, String> getProps() {
		return props;
	}

}
