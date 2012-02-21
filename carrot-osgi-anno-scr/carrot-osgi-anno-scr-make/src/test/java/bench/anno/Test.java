package bench.anno;

import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

public class Test {

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {

		// ASM Tree API
		final ClassNode klazNone = new ClassNode();

		// Read the class byte code from stream
		// final ClassReader klazReader = new ClassReader(
		// ClassLoader
		// .getSystemResourceAsStream("bench/anno/Source.class"));

		final ClassReader klazReader = new ClassReader(Source.class.getName());

		klazReader.accept(klazNone, 0);

		System.out.println("Class Name : " + klazNone.name);
		System.out.println("Source File : " + klazNone.sourceFile);

		// Annotations with RetentionPolicy.CLASS
		// @Retention(RetentionPolicy.CLASS)
		System.out.println("\n==Invisible Annotations==");
		printAnnotation(klazNone.invisibleAnnotations);

		// Annotations with RetentionPolicy.RUNTIME
		// @Retention(RetentionPolicy.RUNTIME)
		System.out.println("\n==Visible Annotations==");
		printAnnotation(klazNone.visibleAnnotations);
	}

	static void printAnnotation(final List annotationList) {

		if (annotationList != null && !annotationList.isEmpty()) {

			AnnotationNode annoNode = null;

			for (final Object annotation : annotationList) {

				annoNode = (AnnotationNode) annotation;

				System.out.println("Annotation Descriptor : " + annoNode.desc);
				System.out.println("Annotation attribute pairs : "
						+ annoNode.values);

				final AnnotationVisitor visitor = new AnnotationVisitor() {
					@Override
					public void visit(final String arg0, final Object arg1) {

					}

					@Override
					public AnnotationVisitor visitAnnotation(final String arg0,
							final String arg1) {
						return null;
					}

					@Override
					public AnnotationVisitor visitArray(final String arg0) {
						return null;
					}

					@Override
					public void visitEnd() {

					}

					@Override
					public void visitEnum(final String arg0, final String arg1,
							final String arg2) {

					}

				};

				annoNode.accept(visitor);

			}

		} else {

			System.out.println("No annotations found..");

		}

	}

}