/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.make;

import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

public abstract class Comp0 implements Runnable {

	@Activate
	protected void activate() {

	}

	@Override
	public void run() {

	}

	//

	@Reference
	protected void make(final Runnable task) {
	}

	protected void unmake(final Runnable task) {
	}

	//

	@Reference
	protected void set(final Callable task) {
	}

	protected void unset(final Callable task) {
	}

	//

	@Reference
	protected void bind(final CharSequence task) {
	}

	protected void unbind(final CharSequence task) {
	}

}
