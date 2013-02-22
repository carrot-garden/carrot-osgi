/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package idea;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class DsFieldVisitor extends FieldVisitor{

	public DsFieldVisitor() {
		super(Opcodes.ASM4);
	}

}
