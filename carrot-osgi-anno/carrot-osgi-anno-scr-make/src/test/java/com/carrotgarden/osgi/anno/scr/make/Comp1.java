package com.carrotgarden.osgi.anno.scr.make;

import java.util.concurrent.Future;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

@Component(property = { "hello-string:String=world", "be=happy",
		"wrong:type=wrong type", "size:Integer=100" }, properties = {
		"OSGI-INF/hello.properties", "brand/system.properties" })
public class Comp1 extends Comp0 implements Comparable<String> {

	@Property
	static final String hello = "hello property";

	@Property(name = "good-bye")
	static final String goodBye = "hello property";

	@Override
	public int compareTo(final String o) {
		return 0;
	}

	@Modified
	protected void modified1() {

	}

	@Deactivate
	protected void deactivate1() {

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
