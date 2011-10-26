package com.carrotgarden.osgi.anno.scr.conv;

import static org.osgi.service.component.annotations.ReferenceCardinality.AT_LEAST_ONE;
import static org.osgi.service.component.annotations.ReferenceCardinality.MANDATORY;
import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import org.osgi.service.component.annotations.ReferenceCardinality;

public enum ReferenceCardinalityTag {

	TAG_AT_LEAST_ONE(AT_LEAST_ONE, "1..n"), //

	TAG_MULTIPLE(MULTIPLE, "1..n"), //

	TAG_MANDATORY(MANDATORY, "1..1"), //

	TAG_OPTIONAL(OPTIONAL, "0..1"), //

	;

	public final ReferenceCardinality cardinality;

	public final String tag;

	ReferenceCardinalityTag(final ReferenceCardinality cardinality,
			final String tag) {
		this.cardinality = cardinality;
		this.tag = tag;
	}

	public static ReferenceCardinalityTag from(final String tag) {
		for (final ReferenceCardinalityTag known : values()) {
			if (known.tag.equalsIgnoreCase(tag)) {
				return known;
			}
		}
		return TAG_MANDATORY;
	}

	public static ReferenceCardinalityTag from(
			final ReferenceCardinality cardinality) {
		for (final ReferenceCardinalityTag known : values()) {
			if (known.cardinality == cardinality) {
				return known;
			}
		}
		return TAG_MANDATORY;
	}

}
