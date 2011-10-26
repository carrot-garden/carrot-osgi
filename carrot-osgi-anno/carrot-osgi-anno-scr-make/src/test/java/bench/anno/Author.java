package bench.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permitted types for annotation attributes
 * 
 * 1. primitive type 2. String 3. Class 4. annotation 5. enumeration 6.
 * 1-dimensional arrays
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Author {

	String name() default "admin";

	String creationDate();

}
