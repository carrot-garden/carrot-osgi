package com.carrotgarden.osgi.anno.scr.make;

import java.util.concurrent.Future;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

@Component
public class Comp1 extends Comp0 implements Comparable<String> {

	@Property
	static final String hello = "hello property";

	@Property(name = "good-bye")
	static final String goodBye = "hello property";

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
