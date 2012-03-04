/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.anno;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Author(name = "BTC", creationDate = "01/01/2500")
@History({ "1.0", "2.0", "3.0" })
@Component(name = "hello", immediate = true)
public class Source {

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
