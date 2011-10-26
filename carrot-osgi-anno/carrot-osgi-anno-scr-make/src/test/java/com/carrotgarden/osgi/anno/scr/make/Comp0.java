package com.carrotgarden.osgi.anno.scr.make;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

public abstract class Comp0 implements Runnable {

	@Activate
	protected void activate() {

	}

	@Override
	public void run() {

	}

	@Reference
	protected void task(final Runnable task) {

	}

}
