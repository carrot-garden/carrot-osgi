/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.case02;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;

@Component(
//
// should remove duplicates
service = { Callable.class, Callable.class, Callable.class, Comparable.class },
//
// should override "override" form level 1
property = { "override=level 2", "hello-2:Float=2.5" }
//
)
public class Comp_02_2 extends Comp_02_1 implements Callable<String>, Runnable,
		Comparable<String>, Future<String> {

	// should overrid level 1
	@Property
	static final String OVER = "level 2";

	@Activate
	void activate2() {

	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String get() throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get(final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}
