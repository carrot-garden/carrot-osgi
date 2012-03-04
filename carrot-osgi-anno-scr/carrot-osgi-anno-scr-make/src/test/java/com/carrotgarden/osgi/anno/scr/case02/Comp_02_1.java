/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.case02;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

@Component(
//
// should use String for wrong type
// "override" should be overwriten by level 2
property = { "override=level 1", "hello-string:String=world",
		"be=happy at comp 1", "wrong:type=wrong type", "size:Integer=100" },
//
// should remove duplicates
properties = { "OSGI-INF/hello.properties", "brand/system.properties",
		"brand/system.properties", "brand/system.properties" }
//
)
public class Comp_02_1 extends Comp_02_0 implements Comparable<String>,
		Future<String> {

	@Override
	@Reference(name = "override")
	protected void make(final Runnable task) {
	}

	@Override
	protected void unmake(final Runnable task) {
	}

	// should be overwritten by level 2
	// should use field name
	@Property
	static final String OVER = "level 1";

	@Property
	static final String hello = "hello property";

	// should use provided name instead of field name
	@Property(name = "good-bye")
	static final String goodBye = "hello property";

	@Override
	public int compareTo(final String o) {
		return 0;
	}

	// should override level 0
	@Modified
	protected void modified1() {

	}

	@Deactivate
	protected void deactivate1() {

	}

	//

	// should match comp/uncomp
	@Reference
	protected void comp(final Comparable<?> task) {
	}

	protected void uncomp(final Comparable<?> task) {
	}

	//

	// should match add/remove
	@Reference
	protected void add(final Cloneable task) {
	}

	protected void remove(final Cloneable task) {
	}

	//
	// should match add/remove
	@Reference
	protected void addSomeThing(final Executor task) {
	}

	protected void removeSomeThing(final Executor task) {
	}

	//

	// should match bind/unbind
	@Reference
	protected void bindSome(final Future<?> task) {
	}

	protected void unbindSome(final Future<?> task) {
	}

	// ###################################################

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
