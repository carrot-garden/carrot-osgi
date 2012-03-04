/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.case03;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;

@Component
public class Comp_03_1_props {

	/**
	 * java.lang.IllegalArgumentException: property field must be one of : [
	 * String Long Double Float Integer Byte Character Boolean Short ] class
	 * com.carrotgarden.osgi.anno.scr.case03.Comp_03_1_props / STRING / class
	 * java.lang.Object
	 */

	@Property
	static final String STRING = "string";

	@Property
	static final Long LONG = Long.parseLong("123");

	@Property
	static final Double DOUBLE = 1.5d + Double.parseDouble("-2.5");

	@Property
	static final Float FLOAT = 1.5f + Float.parseFloat("-2.5");

	@Property
	static final Integer INTEGER = (int) (5.5f + Float.parseFloat("-2.5"));

	@Property
	static final Byte BYTE = -1;

	@Property
	static final Character CHAR = 'A';

	@Property
	static final Boolean BOOL = (2 > 1);

	@Property
	static final Short SHORT = (short) (1024 * 1023);

}
