/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.anno.scr.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import anno.AnnoClass;
import anno.AnnoRuntime;

public class UtilAsmTest {

	@AnnoClass
	@AnnoRuntime
	static class ComboClass {
	}

	static class ComboField {
		@AnnoClass
		@AnnoRuntime
		String name;
	}

	static class ComboMethod {
		@AnnoClass
		@AnnoRuntime
		String name() {
			return "name";
		}
	}

	@Test
	public void combineClass() throws Exception {

		ClassNode classNode = UtilAsm.classNode(ComboClass.class);

		List<AnnotationNode> list = UtilAsm.combine(classNode);

		assertEquals(2, list.size());

	}

	@Test
	public void combineField() throws Exception {

		ClassNode classNode = UtilAsm.classNode(ComboField.class);

		FieldNode fieldNode = (FieldNode) classNode.fields.get(0);

		List<AnnotationNode> list = UtilAsm.combine(fieldNode);

		assertEquals(2, list.size());

	}

	@Test
	public void combineMethod() throws Exception {

		ClassNode classNode = UtilAsm.classNode(ComboMethod.class);

		@SuppressWarnings("unchecked")
		List<MethodNode> methodList = classNode.methods;

		MethodNode methodNode = null;
		for (MethodNode node : methodList) {
			if("name".equals(node.name)){
				methodNode = node;
			}
		}

		List<AnnotationNode> list = UtilAsm.combine(methodNode);

		assertEquals(2, list.size());

	}

}
