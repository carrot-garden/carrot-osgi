/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package anno;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

@AnnoClass(nameEntry = "BTC", creationDate = "01/01/2500")
@AnnoRuntime({ "1.0", "2.0", "3.0" })
@Component(//
name = "hello", //
immediate = true, //
property = { "a=b", "c=d" }, //
configurationPolicy = ConfigurationPolicy.OPTIONAL //
)
public class Source {
	
	@Property
	static final String NAME = "value";

	@Reference
	protected void task(final Runnable task) {

	}

	private int fieldOne;
	private String fieldTwo;

	public Source(final int fieldOne, final String fieldTwo) {
		super();
		this.fieldOne = fieldOne;
		this.fieldTwo = fieldTwo;
	}

	public int getFieldOne() {
		return fieldOne;
	}

	public void setFieldOne(final int fieldOne) {
		this.fieldOne = fieldOne;
	}

	public String getFieldTwo() {
		return fieldTwo;
	}

	public void setFieldTwo(final String fieldTwo) {
		this.fieldTwo = fieldTwo;
	}

}
