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
 * Custom DS annotation. Provides "key=value" component property. Must be used
 * on static final fields. Type must be one of OSGI types:
 * <p>
 * String Long Double Float Integer Byte Character Short
 * <p>
 * String property can be multi-value; in this case use "\n" to separate values.
 * 
 * @see Component
 * 
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.FIELD)
public @interface Property {

	/**
	 * Use name() if provided;
	 * <p>
	 * Otherwise, use annotation target final static field name.
	 */
	String name() default "";

}
