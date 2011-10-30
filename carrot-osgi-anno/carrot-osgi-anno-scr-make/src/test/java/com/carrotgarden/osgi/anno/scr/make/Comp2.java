package com.carrotgarden.osgi.anno.scr.make;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;

@Component(service = { Callable.class, Callable.class, Callable.class,
		Comparable.class }, property = { "override=level 2",
		"hello-2:Float=hello from comp 2" })
public class Comp2 extends Comp1 implements Callable, Runnable,
		Comparable<String>, Future<String> {

	@Property
	static final String OVER = "level 2";

	@Activate
	void activate2() {

	}

	@Override
	public Object call() throws Exception {
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
