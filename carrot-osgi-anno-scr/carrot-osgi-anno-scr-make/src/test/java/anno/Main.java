/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package anno;

import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

public class Main {

	static final int SKIP = ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG
			| ClassReader.SKIP_FRAMES;

	/**
	 */
	public static void main(final String[] args) throws Exception {

		/** ASM Tree API */
		final ClassNode klazNode = new ClassNode();

		// Read the class byte code from stream
		// final ClassReader klazReader = new ClassReader(
		// ClassLoader
		// .getSystemResourceAsStream("bench/anno/Source.class"));

		String name = Source.class.getName();

		final ClassReader klazReader = new ClassReader(name);

		klazReader.accept(klazNode, SKIP);

		System.out.println("Class Name : " + klazNode.name);
		System.out.println("Source File : " + klazNode.sourceFile);
		System.out.println("Super name : " + klazNode.superName);

		// klazNode.accept(new ClassPrinter());

		klazReader.accept(new ClassPrinter(), SKIP);

		klazReader.accept(klazVisitor, SKIP);

		// klazNode.accept(klazVisitor);

		// Annotations with RetentionPolicy.CLASS
		// @Retention(RetentionPolicy.CLASS)
		System.out.println("# Invisible Annotations");
		printAnno(klazNode.invisibleAnnotations);

		// Annotations with RetentionPolicy.RUNTIME
		// @Retention(RetentionPolicy.RUNTIME)
		System.out.println("# Visible Annotations");
		printAnno(klazNode.visibleAnnotations);

	}

	static final FieldVisitor fieldVisitor = new FieldVisitor(Opcodes.ASM4) {

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			String refe = Type.getDescriptor(Property.class);
			System.out.println("# f-anno=" + desc.equals(refe));
			return super.visitAnnotation(desc, visible);
		}

	};

	static final MethodVisitor methodVisitor = new MethodVisitor(Opcodes.ASM4) {

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			String refe = Type.getDescriptor(Reference.class);
			System.out.println("# m-anno=" + desc.equals(refe));
			return super.visitAnnotation(desc, visible);
		}

	};

	static final ClassVisitor klazVisitor = new ClassVisitor(Opcodes.ASM4) {

		public FieldVisitor visitField(int access, String name, String desc,
				String signature, Object value) {
			System.out.println("# field=" + name);
			return fieldVisitor;
		}

		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			System.out.println("# method=" + name);
			return methodVisitor;
		}

	};

	static final AnnotationVisitor annoVisitor = new AnnotationVisitor(
			Opcodes.ASM4) {

		@Override
		public void visit(final String name, final Object value) {
			System.out.println("# name=" + name + " value=" + value);
		}

		@Override
		public AnnotationVisitor visitAnnotation(final String value,
				final String desc) {
			System.out.println("# desc=" + desc + " value=" + value);
			return null;
		}

		@Override
		public AnnotationVisitor visitArray(final String value) {
			System.out.println("# value=" + value);
			return this;
		}

		@Override
		public void visitEnd() {
			System.out.println("# end");
		}

		@Override
		public void visitEnum(final String name, final String desc,
				final String value) {
			System.out.println("# name=" + name + " desc=" + desc + " value="
					+ value);
		}

	};

	static void printAnno(final List<AnnotationNode> annotationList) {

		if (annotationList == null || annotationList.isEmpty()) {
			System.out.println("No annotations found.");
			return;
		}

		for (final AnnotationNode annoNode : annotationList) {

			System.out.println("Anno Desc   : " + annoNode.desc);
			System.out.println("Anno Values : " + annoNode.values);

			annoNode.accept(annoVisitor);

		}

	}

}