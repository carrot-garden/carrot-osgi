package idea;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.util.UtilAsm;

public class DsClassVisitor extends ClassVisitor {

	private final ComponentBean component;

	public DsClassVisitor(final ComponentBean component) {
		super(Opcodes.ASM4);
		this.component = component;
	}

	final FieldVisitor fieldVisitor = new FieldVisitor(Opcodes.ASM4) {

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

			if (UtilAsm.isPropertyDesc(desc)) {

			}

			return null;

		}

	};

	final MethodVisitor methodVisitor = new MethodVisitor(Opcodes.ASM4) {

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

			return null;

		}

	};

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

}
