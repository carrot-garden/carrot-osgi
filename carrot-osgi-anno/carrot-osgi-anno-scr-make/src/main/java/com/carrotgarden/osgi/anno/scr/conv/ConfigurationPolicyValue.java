package com.carrotgarden.osgi.anno.scr.conv;

import static org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE;
import static org.osgi.service.component.annotations.ConfigurationPolicy.OPTIONAL;
import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;

import org.osgi.service.component.annotations.ConfigurationPolicy;

public enum ConfigurationPolicyValue {

	TAG_IGNORE(IGNORE, "ignore"), //

	TAG_OPTIONAL(OPTIONAL, "optional"), //

	TAG_REQUIRE(REQUIRE, "require"), //

	;

	public final ConfigurationPolicy policy;

	public final String value;

	ConfigurationPolicyValue(final ConfigurationPolicy policy,
			final String value) {
		this.policy = policy;
		this.value = value;
	}

	public static ConfigurationPolicyValue from(final String value) {
		for (final ConfigurationPolicyValue known : values()) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}
		return TAG_OPTIONAL;
	}

	public static ConfigurationPolicyValue from(final ConfigurationPolicy policy) {
		for (final ConfigurationPolicyValue known : values()) {
			if (known.policy == policy) {
				return known;
			}
		}
		return TAG_OPTIONAL;
	}

}
