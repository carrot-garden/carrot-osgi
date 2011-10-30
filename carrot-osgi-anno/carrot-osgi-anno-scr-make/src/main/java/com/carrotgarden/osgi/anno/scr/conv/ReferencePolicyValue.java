package com.carrotgarden.osgi.anno.scr.conv;

import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;
import static org.osgi.service.component.annotations.ReferencePolicy.STATIC;

import org.osgi.service.component.annotations.ReferencePolicy;

public enum ReferencePolicyValue {

	_STATIC_(STATIC, "static"), //

	_DYNAMIC_(DYNAMIC, "dynamic"), //

	;

	public final ReferencePolicy policy;

	public final String value;

	ReferencePolicyValue(final ReferencePolicy policy, final String value) {
		this.policy = policy;
		this.value = value;
	}

	public static ReferencePolicyValue from(final String value) {
		for (final ReferencePolicyValue known : values()) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}
		return _STATIC_;
	}

	public static ReferencePolicyValue from(final ReferencePolicy policy) {
		for (final ReferencePolicyValue known : values()) {
			if (known.policy == policy) {
				return known;
			}
		}
		return _STATIC_;
	}

}
