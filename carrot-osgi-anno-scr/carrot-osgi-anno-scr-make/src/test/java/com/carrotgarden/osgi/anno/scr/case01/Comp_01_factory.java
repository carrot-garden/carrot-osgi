package com.carrotgarden.osgi.anno.scr.case01;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(factory = "factory uuid")
public class Comp_01_factory implements Runnable {

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
