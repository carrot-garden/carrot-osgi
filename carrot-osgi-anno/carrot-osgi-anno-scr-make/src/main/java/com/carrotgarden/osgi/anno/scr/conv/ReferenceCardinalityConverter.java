package com.carrotgarden.osgi.anno.scr.conv;

import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ReferenceCardinalityConverter extends AbstractSingleValueConverter {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean canConvert(final Class klaz) {

		return klaz.equals(ReferenceCardinality.class);
	}

	@Override
	public String toString(final Object value) {

		final ReferenceCardinalityTag policyTag = ReferenceCardinalityTag
				.from((ReferenceCardinality) value);

		return policyTag.tag;

	}

	@Override
	public Object fromString(final String text) {

		final ReferenceCardinalityTag policyTag = ReferenceCardinalityTag
				.from(text);

		return policyTag.cardinality;

	}

}