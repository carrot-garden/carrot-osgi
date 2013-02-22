/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.conv;

import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.anno.scr.util.UtilEnum;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Xstream enum converter for {@link ConfigurationPolicy}.
 */
public class ConfigurationPolicyConverter extends AbstractSingleValueConverter {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(final Class klaz) {

		return klaz.equals(ConfigurationPolicy.class);

	}

	@Override
	public String toString(final Object instance) {

		final ConfigurationPolicy policy = (ConfigurationPolicy) instance;

		return policy.toString();

	}

	@Override
	public Object fromString(final String value) {

		final ConfigurationPolicy policy = UtilEnum
				.fromConfigurationPolicy(value);

		return policy;

	}

}
