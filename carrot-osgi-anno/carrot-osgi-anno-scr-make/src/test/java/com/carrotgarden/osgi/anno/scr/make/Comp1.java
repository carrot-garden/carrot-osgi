package com.carrotgarden.osgi.anno.scr.make;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class Comp1 extends Comp0 implements Comparable<String> {

	@Override
	public int compareTo(final String o) {
		return 0;
	}

	@Reference
	protected void comp(final Comparable task) {

	}

}
