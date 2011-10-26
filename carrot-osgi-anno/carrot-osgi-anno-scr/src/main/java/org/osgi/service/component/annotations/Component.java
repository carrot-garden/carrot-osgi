package org.osgi.service.component.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
public @interface Component {

	String name() default "";

	Class<?>[] service() default {};

	String factory() default "";

	boolean servicefactory() default false;

	boolean enabled() default true;

	boolean immediate() default false;

	ConfigurationPolicy configurationPolicy() default ConfigurationPolicy.OPTIONAL;

	String[] property() default {};

	String[] properties() default {};

}
