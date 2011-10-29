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
