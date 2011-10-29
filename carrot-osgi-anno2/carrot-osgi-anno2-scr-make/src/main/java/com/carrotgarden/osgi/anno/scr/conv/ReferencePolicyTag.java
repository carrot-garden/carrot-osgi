package com.carrotgarden.osgi.anno.scr.conv;

import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;
import static org.osgi.service.component.annotations.ReferencePolicy.STATIC;

import org.osgi.service.component.annotations.ReferencePolicy;

public enum ReferencePolicyTag {

	_STATIC_(STATIC, "static"), //

	_DYNAMIC_(DYNAMIC, "dynamic"), //

	;

	public final ReferencePolicy policy;

	public final String tag;

	ReferencePolicyTag(final ReferencePolicy policy, final String tag) {
		this.policy = policy;
		this.tag = tag;
	}

	public static ReferencePolicyTag from(final String tag) {
		for (final ReferencePolicyTag known : values()) {
			if (known.tag.equalsIgnoreCase(tag)) {
				return known;
			}
		}
		return _STATIC_;
	}

	public static ReferencePolicyTag from(final ReferencePolicy policy) {
		for (final ReferencePolicyTag known : values()) {
			if (known.policy == policy) {
				return known;
			}
		}
		return _STATIC_;
	}

}
