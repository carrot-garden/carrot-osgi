/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.osgi.service.component.annotations;

/**
 * Cardinality for the Reference annotation. Specifies if the reference is
 * optional and if the component implementation support a single bound service
 * or multiple bound services.
 */
public enum ReferenceCardinality {

	/**
	 * The reference is mandatory and multiple. That is, the reference has a
	 * cardinality of 1..n.
	 */
	AT_LEAST_ONE("1..n"), //

	/**
	 * The reference is optional and multiple. That is, the reference has a
	 * cardinality of 0..n.
	 */
	MULTIPLE("0..n"), //

	/**
	 * The reference is mandatory and unary. That is, the reference has a
	 * cardinality of 1..1.
	 */
	MANDATORY("1..1"), // default

	/**
	 * The reference is optional and unary. That is, the reference has a
	 * cardinality of 0..1.
	 */
	OPTIONAL("0..1"), //

	;

	/** component descriptor xml value */
	public final String value;

	/** component descriptor xml value */
	public String value() {
		return value;
	}

	private ReferenceCardinality(final String value) {
		this.value = value;
	}

	private static final ReferenceCardinality[] ENUM_VALS = values();

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * MANDATORY if not resolved
	 */
	public static ReferenceCardinality from(final String value) {

		for (final ReferenceCardinality known : ENUM_VALS) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}

		return MANDATORY;

	}

}
