/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.case01;

import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class Comp_01_reference implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	//

	@SuppressWarnings("unused")
	private Comparable<?> comparator;

	@Reference
	protected void bind(final Comparable<?> comparator) {

	}

	protected void unbind(final Comparable<?> comparator) {

	}

	//

	@SuppressWarnings("unused")
	private ActionListener listener;

	@Reference
	protected void addListener(final ActionListener listener) {

	}

	protected void removeListener(final ActionListener listener) {

	}

	//

	/**
	 * <a href="https://github.com/carrot-garden/carrot-osgi/issues/1">Bind
	 * Method Signature must accept 2 parameters</a>
	 */

	@SuppressWarnings("unused")
	private Callable<?> caller;

	@Reference
	protected void bind(final Callable<?> caller, final Map<?, ?> params) {

	}

	protected void unbind(final Callable<?> caller, final Map<?, ?> params) {

	}

}
