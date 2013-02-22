/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permitted types for annotation attributes
 * 
 * 1. primitive type
 * 
 * 2. String
 * 
 * 3. Class
 * 
 * 4. annotation
 * 
 * 5. enumeration
 * 
 * 6. 1-dimensional arrays
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AnnoClass {

	String nameEntry() default "admin";

	String creationDate();

}
