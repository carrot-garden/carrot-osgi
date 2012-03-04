/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.osgi.service.component.annotations;

/**
 * Configuration Policy for the Component annotation. Controls whether component
 * configurations must be satisfied depending on the presence of a corresponding
 * Configuration object in the OSGi Configuration Admin service. A corresponding
 * configuration is a Configuration object where the PID is the name of the
 * component.
 */
public enum ConfigurationPolicy {

	/**
	 * Always allow the component configuration to be satisfied and do not use
	 * the corresponding Configuration object even if it is present.
	 */
	IGNORE("ignore"), //

	/**
	 * Use the corresponding Configuration object if present but allow the
	 * component to be satisfied even if the corresponding Configuration object
	 * is not present.
	 */
	OPTIONAL("optional"), // default

	/**
	 * There must be a corresponding Configuration object for the component
	 * configuration to become satisfied.
	 */
	REQUIRE("require"), //

	;

	/** component descriptor xml value */
	public final String value;

	/** component descriptor xml value */
	public String value() {
		return value;
	}

	private ConfigurationPolicy(final String value) {
		this.value = value;
	}

	private static final ConfigurationPolicy[] ENUM_VALS = values();

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * OPTIONAL if not resolved
	 */
	public static ConfigurationPolicy from(final String value) {

		for (final ConfigurationPolicy known : ENUM_VALS) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}

		return OPTIONAL;

	}

}
