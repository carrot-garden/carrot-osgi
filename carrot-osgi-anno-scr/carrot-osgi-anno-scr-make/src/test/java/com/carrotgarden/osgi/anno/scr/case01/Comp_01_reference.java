package com.carrotgarden.osgi.anno.scr.case01;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class Comp_01_reference implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	private Comparable<?> comparator;

	@Reference
	protected void bind(final Comparable<?> comparator) {

	}

	protected void unbind(final Comparable<?> comparator) {

	}

}
