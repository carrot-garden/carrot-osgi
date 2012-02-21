package org.osgi.service.component.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * not in osgi spec yet;
 * 
 * must be used on static final String fields;
 * 
 * @see Component
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Property {

	/**
	 * use name() if provided;
	 * 
	 * use target final static String field name otherwise
	 */
	String name() default "";

}
