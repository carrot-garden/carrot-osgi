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
import com.carrotgarden.osgi.anno.scr.util.UtilJdk;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Xstream enum converter for {@link ReferencePolicy}.
 */
public class ReferencePolicyConverter extends AbstractSingleValueConverter {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(final Class klaz) {

		return klaz.equals(ReferencePolicy.class);

	}

	@Override
	public String toString(final Object instance) {

		final ReferencePolicy entry = (ReferencePolicy) instance;

		/** Workaround for OSGI final specification change. */
		final String value = UtilJdk.readField(entry, "value");

		return value;

	}

	@Override
	public Object fromString(final String value) {

		final ReferencePolicy policy = UtilEnum.fromReferencePolicy(value);

		return policy;

	}

}
