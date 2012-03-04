/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.conv;

import static org.osgi.service.component.annotations.ConfigurationPolicy.IGNORE;
import static org.osgi.service.component.annotations.ConfigurationPolicy.OPTIONAL;
import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;

import org.osgi.service.component.annotations.ConfigurationPolicy;

public enum ConfigurationPolicyTag {

	TAG_IGNORE(IGNORE, "ignore"), //

	TAG_OPTIONAL(OPTIONAL, "optional"), //

	TAG_REQUIRE(REQUIRE, "optional"), //

	;

	public final ConfigurationPolicy policy;

	public final String tag;

	ConfigurationPolicyTag(final ConfigurationPolicy policy, final String tag) {
		this.policy = policy;
		this.tag = tag;
	}

	public static ConfigurationPolicyTag from(final String tag) {
		for (final ConfigurationPolicyTag known : values()) {
			if (known.tag.equalsIgnoreCase(tag)) {
				return known;
			}
		}
		return TAG_OPTIONAL;
	}

	public static ConfigurationPolicyTag from(final ConfigurationPolicy policy) {
		for (final ConfigurationPolicyTag known : values()) {
			if (known.policy == policy) {
				return known;
			}
		}
		return TAG_OPTIONAL;
	}

}
