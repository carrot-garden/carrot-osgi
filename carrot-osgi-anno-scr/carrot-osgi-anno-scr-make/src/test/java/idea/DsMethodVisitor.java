/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package idea;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.util.Util;
import com.carrotgarden.osgi.anno.scr.util.UtilAsm;

public class DsMethodVisitor extends MethodVisitor {

	private final ComponentBean component;

	public DsMethodVisitor(final ComponentBean component) {
		super(Opcodes.ASM4);
		this.component = component;
	}

	private int countActivate;
	private int countDeactivate;
	private int countModified;

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

		if (UtilAsm.isActivateDesc(desc)) {
			Util.assertZeroCount(countActivate, "duplicate @Activate");
			countActivate++;
			return null;
		}

		if (UtilAsm.isDeactivateDesc(desc)) {
			Util.assertZeroCount(countDeactivate, "duplicate @Deactivate");
			countDeactivate++;
			return null;
		}

		if (UtilAsm.isModifiedDesc(desc)) {
			Util.assertZeroCount(countModified, "duplicate @Modified");
			countModified++;
			return null;
		}

		return null;
		
	}

}
