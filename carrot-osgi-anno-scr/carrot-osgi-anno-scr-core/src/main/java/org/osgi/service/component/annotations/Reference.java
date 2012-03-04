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

/**
 * Identify the annotated method as a bind method of a Service Component. The
 * annotated method is a bind method of the Component. This annotation is not
 * processed at runtime by a Service Component Runtime implementation. It must
 * be processed by tools and used to add a Component Description to the bundle.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Reference {

	/**
	 * The cardinality of the reference. If not specified, the reference has a
	 * 1..1 cardinality.
	 */
	ReferenceCardinality cardinality() default ReferenceCardinality.MANDATORY;

	/**
	 * The name of this reference. If not specified, the name of this reference
	 * is based upon the name of the method being annotated. If the method name
	 * begins with set or add, that is removed.
	 */
	String name() default "";

	/**
	 * The policy for the reference. If not specified, the STATIC reference
	 * policy is used.
	 */
	ReferencePolicy policy() default ReferencePolicy.STATIC;

	/**
	 * The type of the service to bind to this reference. If not specified, the
	 * type of the service to bind is based upon the type of the first argument
	 * of the method being annotated.
	 */
	Class<?> service() default Object.class;

	/** The target filter for the reference. */
	String target() default "";

	/**
	 * The name of the unbind method which pairs with the annotated bind method.
	 * To declare no unbind method, the value "-" must be used. If not
	 * specified, the name of the unbind method is derived from the name of the
	 * annotated bind method. If the annotated method name begins with set, that
	 * is replaced with unset to derive the unbind method name. If the annotated
	 * method name begins with add, that is replaced with remove to derive the
	 * unbind method name. Otherwise, un is prefixed to the annotated method
	 * name to derive the unbind method name.
	 */
	String unbind() default "";

}
