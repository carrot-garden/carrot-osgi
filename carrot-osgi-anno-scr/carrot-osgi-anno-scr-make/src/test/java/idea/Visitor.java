package idea;

import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.bean.ReferenceBean;
import com.carrotgarden.osgi.anno.scr.util.Util;
import com.carrotgarden.osgi.anno.scr.util.UtilAsm;

import org.osgi.service.component.annotations.*;

public class Visitor extends ClassVisitor {

	private final ComponentBean component;

	public Visitor(final ComponentBean component) {
		super(Opcodes.ASM4);
		this.component = component;
	}

	private int access;
	private String name;
	private String desc;
	private String signature;

	private final FieldVisitor fieldVisitor = new FieldVisitor(Opcodes.ASM4) {

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

			return null;

		}

	};

	private int countActivate;
	private int countDeactivate;
	private int countModified;

	private final MethodVisitor methodVisitor = new MethodVisitor(Opcodes.ASM4) {

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

			if (UtilAsm.isActivateDesc(desc)) {
				Util.assertZeroCount(countActivate++, "duplicate @Activate");
				component.activate = name;
				return null;
			}

			if (UtilAsm.isDeactivateDesc(desc)) {
				Util.assertZeroCount(countDeactivate++, "duplicate @Deactivate");
				component.deactivate = name;
				return null;
			}

			if (UtilAsm.isModifiedDesc(desc)) {
				Util.assertZeroCount(countModified++, "duplicate @Modified");
				component.modified = name;
				return null;
			}

			if (UtilAsm.isReferenceDesc(desc)) {
				return referenceVisitor;
			}

			return null;

		}

	};

	/**
	 * See {@link Reference}
	 */
	private final AnnotationVisitor referenceVisitor = new AnnotationVisitor(
			Opcodes.ASM4) {

		public void visit(String name, Object value) {

			Set<ReferenceBean> referenceSet = component.referenceSet;

			if ("name".equals(name)) {
				
			}
			
		}

	};

	private void storeState(int access, String name, String desc,
			String signature) {
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
	}

	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		System.out.println("# field=" + name);
		storeState(access, name, desc, signature);
		return fieldVisitor;
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		System.out.println("# method=" + name);
		storeState(access, name, desc, signature);
		return methodVisitor;
	}

}
