package com.carrotgarden.osgi.anno.scr.conv;

import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ReferencePolicyConverter extends AbstractSingleValueConverter {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean canConvert(final Class klaz) {

		return klaz.equals(ReferencePolicy.class);
	}

	@Override
	public String toString(final Object instance) {

		final ReferencePolicy policy = (ReferencePolicy) instance;

		return policy.value;

	}

	@Override
	public Object fromString(final String value) {

		final ReferencePolicy policy = ReferencePolicy.from(value);

		return policy;

	}

}
