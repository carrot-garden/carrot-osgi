package com.carrotgarden.osgi.anno.scr.conv;

import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ConfigurationPolicyConverter extends AbstractSingleValueConverter {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean canConvert(final Class klaz) {

		// log.debug("klaz : {}", klaz);

		return klaz.equals(ConfigurationPolicy.class);
	}

	@Override
	public String toString(final Object instance) {

		final ConfigurationPolicy policy = (ConfigurationPolicy) instance;

		return policy.value;

	}

	@Override
	public Object fromString(final String value) {

		final ConfigurationPolicy policy = ConfigurationPolicy.from(value);

		return policy;

	}

}
