/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.case01;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class Comp_01_reference_dynamic implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	private Comparable<?> comparator;

	@Reference( //
	cardinality = ReferenceCardinality.MULTIPLE, //
	policy = ReferencePolicy.DYNAMIC //
	)
	protected void bind(final Comparable<?> comparator) {

	}

	protected void unbind(final Comparable<?> comparator) {

	}

}
