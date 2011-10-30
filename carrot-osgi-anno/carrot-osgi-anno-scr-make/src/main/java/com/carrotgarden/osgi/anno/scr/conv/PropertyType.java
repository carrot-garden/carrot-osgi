package com.carrotgarden.osgi.anno.scr.conv;

public enum PropertyType {

	STRING("String"), // default

	LONG("Long"), //

	DOUBLE("Double"), //

	FLOAT("Float"), //

	INTEGER("Integer"), //

	BYTE("Byte"), //

	CHARACTER("Character"), //

	BOOLEAN("Boolean"), //

	SHORT("Short"), //

	;

	public String value;

	private PropertyType(final String value) {
		this.value = value;
	}

	private static final PropertyType[] ENUM_VALS = values();

	public static PropertyType from(final String value) {

		for (final PropertyType known : ENUM_VALS) {
			if (known.value.equalsIgnoreCase(value)) {
				return known;
			}
		}

		return STRING;

	}

}
