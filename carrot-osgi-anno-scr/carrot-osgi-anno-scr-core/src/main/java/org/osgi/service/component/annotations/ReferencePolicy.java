/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.osgi.service.component.annotations;

/** Policy for the Reference annotation. */
public enum ReferencePolicy {

	/**
	 * The static policy is the most simple policy and is the default policy. A
	 * component instance never sees any of the dynamics. Component
	 * configurations are deactivated before any bound service for a reference
	 * having a static policy becomes unavailable. If a target service is
	 * available to replace the bound service which became unavailable, the
	 * component configuration must be reactivated and bound to the replacement
	 * service.
	 */
	STATIC("static"), // default

	/**
	 * The dynamic policy is slightly more complex since the component
	 * implementation must properly handle changes in the set of bound services.
	 * With the dynamic policy, SCR can change the set of bound services without
	 * deactivating a component configuration. If the component uses the event
	 * strategy to access services, then the component instance will be notified
	 * of changes in the set of bound services by calls to the bind and unbind
	 * methods.
	 */
	DYNAMIC("dynamic"), //

	;

	/** component descriptor xml value */
	public final String value;

	/** component descriptor xml value */
	public String value() {
		return value;
	}

	private ReferencePolicy(final String value) {
		this.value = value;
	}

	private static final ReferencePolicy[] ENUM_VALS = values();

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * STATIC if not resolved
	 */
	public static ReferencePolicy from(final String value) {

		for (final ReferencePolicy known : ENUM_VALS) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}

		return STATIC;

	}

}
