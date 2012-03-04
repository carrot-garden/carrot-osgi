/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
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

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface Reference {

	String name() default "";

	Class<?> service() default Object.class;

	ReferenceCardinality cardinality() default ReferenceCardinality.MANDATORY;

	ReferencePolicy policy() default ReferencePolicy.STATIC;

	String target() default "";

	String unbind() default "";

}
