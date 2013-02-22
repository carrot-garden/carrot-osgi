package com.carrotgarden.osgi.anno.scr.util;

import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Enum features missing from OSGI spec.
 */
public class UtilEnum {
	
	private static final ConfigurationPolicy[] ENUM_ConfigurationPolicy = ConfigurationPolicy
	.values();
	
	private static final ReferenceCardinality[] ENUM_ReferenceCardinality = ReferenceCardinality
	.values();
	
	private static final ReferencePolicy[] ENUM_ReferencePolicy = ReferencePolicy
	.values();

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * OPTIONAL if not resolved
	 */
	public static ConfigurationPolicy fromConfigurationPolicy(final String value) {
	
		for (final ConfigurationPolicy known : ENUM_ConfigurationPolicy) {
			if (known.toString().equalsIgnoreCase(value)) {
				return known;
			}
		}
	
		return ConfigurationPolicy.OPTIONAL;
	
	}

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * MANDATORY if not resolved
	 */
	public static ReferenceCardinality fromReferenceCardinality(
			final String value) {
	
		for (final ReferenceCardinality known : ENUM_ReferenceCardinality) {
			if (known.toString().equalsIgnoreCase(value)) {
				return known;
			}
		}
	
		return ReferenceCardinality.MANDATORY;
	
	}

	/**
	 * resolve from component descriptor xml value; ignore case; return default
	 * STATIC if not resolved
	 */
	public static ReferencePolicy fromReferencePolicy(final String value) {
	
		for (final ReferencePolicy known : ENUM_ReferencePolicy) {
			if (known.toString().equalsIgnoreCase(value)) {
				return known;
			}
		}
	
		return ReferencePolicy.STATIC;
	
	}

	private UtilEnum(){
		
	}

}
