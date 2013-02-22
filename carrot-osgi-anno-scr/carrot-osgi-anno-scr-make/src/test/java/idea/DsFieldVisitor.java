package idea;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class DsFieldVisitor extends FieldVisitor{

	public DsFieldVisitor() {
		super(Opcodes.ASM4);
	}

}
