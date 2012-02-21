package com.carrotgarden.osgi.anno.scr.case02;

import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

//should not produce xml; since abstract
public abstract class Comp_02_0 implements Runnable {

	@Activate
	protected void activate0() {

	}

	@Modified
	protected void modified0() {

	}

	//

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
	protected void set(final Callable<?> task) {
	}

	protected void unset(final Callable<?> task) {
	}

	//

	@Reference
	protected void bind(final CharSequence task) {
	}

	protected void unbind(final CharSequence task) {
	}

}
