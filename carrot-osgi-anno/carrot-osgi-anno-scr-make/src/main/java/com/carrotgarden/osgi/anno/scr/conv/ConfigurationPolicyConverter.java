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
	public String toString(final Object value) {

		final ConfigurationPolicyTag policyTag = ConfigurationPolicyTag
				.from((ConfigurationPolicy) value);

		return policyTag.tag;

	}

	@Override
	public Object fromString(final String text) {

		final ConfigurationPolicyTag policyTag = ConfigurationPolicyTag
				.from(text);

		return policyTag.policy;

	}

}