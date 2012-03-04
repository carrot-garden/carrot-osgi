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
 * Identify the annotated class as a Service Component. The annotated class is
 * the implementation class of the Component. This annotation is not processed
 * at runtime by a Service Component Runtime implementation. It must be
 * processed by tools and used to add a Component Description to the bundle.
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Component {

	/**
	 * The configuration policy of this Component. Controls whether component
	 * configurations must be satisfied depending on the presence of a
	 * corresponding Configuration object in the OSGi Configuration Admin
	 * service. A corresponding configuration is a Configuration object where
	 * the PID equals the name of the component. If not specified, the OPTIONAL
	 * configuration policy is used.
	 */
	ConfigurationPolicy configurationPolicy() default ConfigurationPolicy.OPTIONAL;

	/**
	 * Declares whether this Component is enabled when the bundle containing it
	 * is started. If true, this Component is enabled. If false or not
	 * specified, this Component is disabled.
	 */
	boolean enabled() default true;

	/**
	 * The factory identifier of this Component. Specifying a factory identifier
	 * makes this Component a Factory Component. If not specified, the default
	 * is that this Component is not a Factory Component.
	 */
	String factory() default "";

	/**
	 * Declares whether this Component must be immediately activated upon
	 * becoming satisfied or whether activation should be delayed. If true, this
	 * Component must be immediately activated upon becoming satisfied. If
	 * false, activation of this Component is delayed. If this property is
	 * specified, its value must be false if the factory() property is also
	 * specified or must be true if the service() property is specified with an
	 * empty value. If not specified, the default is false if the factory()
	 * property is specified or the service() property is not specified or
	 * specified with a non-empty value and true otherwise.
	 */
	boolean immediate() default false;

	/**
	 * The name of this Component. If not specified, the name of this Component
	 * is the fully qualified type name of the class being annotated.
	 */
	String name() default "";

	/**
	 * Property entries for this Component. Specifies the name of an entry in
	 * the bundle whose contents conform to a standard Java Properties File. The
	 * entry is read and processed to obtain the properties and their values.
	 */
	String[] properties() default {};

	/**
	 * Properties for this Component. Each property string is specified as
	 * "key=value". The type of the property value can be specified in the key
	 * as key:type=value. The type must be one of the property types supported
	 * by the type attribute of the property element of a Component Description.
	 * To specify a property with multiple values, use multiple key, value
	 * pairs. For example, "foo=bar", "foo=baz".
	 */
	String[] property() default {};

	/**
	 * The types under which to register this Component as a service. If no
	 * service should be registered, the empty value {} must be specified. If
	 * not specified, the service types for this Component are all the directly
	 * implemented interfaces of the class being annotated.
	 */
	Class<?>[] service() default {};

	/**
	 * Declares whether this Component uses the OSGi ServiceFactory concept and
	 * each bundle using this Component's service will receive a different
	 * component instance. If true, this Component uses the OSGi ServiceFactory
	 * concept. If false or not specified, this Component does not use the OSGi
	 * ServiceFactory concept.
	 */
	boolean servicefactory() default false;

}
