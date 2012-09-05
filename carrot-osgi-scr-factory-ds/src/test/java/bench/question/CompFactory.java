/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.question;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;

@Component(factory = CompFactory.FACTORY)
public class CompFactory implements Comp {

	public static final String FACTORY = "bench.factory";

	@Property(name = "custom-value")
	static final String VALUE = "very important property value";

}
