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
import org.osgi.service.component.annotations.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.factory.api.FactoryDescriptor;

/** factory component */
@Component(factory = TidgetComponent1.FACTORY)
public class TidgetComponent1 implements Tidget {

	@Property(name = "interface")
	static final String TARGET = Tidget.class.getName();

	@FactoryDescriptor
	private static final Map<String, String> descriptor;
	static {
		descriptor = new HashMap<String, String>();
		descriptor.put("name", "tester-factory-1");
	}

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static final String FACTORY = "abe7c3e4-5db8-4ad7-9721-767d36a7138d";

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
