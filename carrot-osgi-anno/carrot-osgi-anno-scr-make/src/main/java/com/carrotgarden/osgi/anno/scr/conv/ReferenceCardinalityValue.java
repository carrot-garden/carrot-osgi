package com.carrotgarden.osgi.anno.scr.conv;

import static org.osgi.service.component.annotations.ReferenceCardinality.AT_LEAST_ONE;
import static org.osgi.service.component.annotations.ReferenceCardinality.MANDATORY;
import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import org.osgi.service.component.annotations.ReferenceCardinality;

public enum ReferenceCardinalityValue {

	TAG_AT_LEAST_ONE(AT_LEAST_ONE, "1..n"), //

	TAG_MULTIPLE(MULTIPLE, "0..n"), //

	TAG_MANDATORY(MANDATORY, "1..1"), //

	TAG_OPTIONAL(OPTIONAL, "0..1"), //

	;

	public final ReferenceCardinality cardinality;

	public final String value;

	ReferenceCardinalityValue(final ReferenceCardinality cardinality,
			final String value) {
		this.cardinality = cardinality;
		this.value = value;
	}

	public static ReferenceCardinalityValue from(final String value) {
		for (final ReferenceCardinalityValue known : values()) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}
		return TAG_MANDATORY;
	}

	public static ReferenceCardinalityValue from(
			final ReferenceCardinality cardinality) {
		for (final ReferenceCardinalityValue known : values()) {
			if (known.cardinality == cardinality) {
				return known;
			}
		}
		return TAG_MANDATORY;
	}

}
