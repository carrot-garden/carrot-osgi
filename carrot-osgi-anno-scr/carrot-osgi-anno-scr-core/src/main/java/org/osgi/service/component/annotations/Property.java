/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.osgi.service.component.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * not in osgi spec yet;
 * 
 * must be used on static final fields;
 * 
 * type must be one of osgi type:
 * 
 * String Long Double Float Integer Byte Character Short
 * 
 * String property can be multi-value; in this case use "\n" to separate values
 * 
 * @see Component
 * 
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.FIELD)
public @interface Property {

	/**
	 * use name() if provided;
	 * 
	 * use annotation target final static field name otherwise
	 */
	String name() default "";

}
