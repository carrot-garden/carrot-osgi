/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.make;

import java.util.concurrent.Future;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class Comp1 extends Comp0 implements Comparable<String> {

	@Override
	public int compareTo(final String o) {
		return 0;
	}

	//

	@Reference
	protected void comp(final Comparable task) {
	}

	protected void uncomp(final Comparable task) {
	}

	//

	@Reference
	protected void add(final Cloneable task) {
	}

	protected void remove(final Cloneable task) {
	}

	//

	@Reference
	protected void bindSome(final Future task) {
	}

	protected void unbindSome(final Future task) {
	}

}
