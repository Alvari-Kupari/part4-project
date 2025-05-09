package uni.auckland.dep.jens;

import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;

import java.util.Collections;
import java.util.Set;

import static uni.auckland.dep.jens.DataPreProcessing.excludeFirstCharacter;
import static uni.auckland.dep.jens.DataPreProcessing.notPrimitive;

/**
 * Visitor to record field writes.
 * @author jens dietrich
 */
class RecordCallSitesVisitor extends ClassVisitor {
    private Set<String> callSites;
    private Set<String> internalClasses;
    private String currentClass = null;
    private String currentMethod = null;

    public RecordCallSitesVisitor(Set<String> callSites, Set<String> internalClasses) {
        super(Opcodes.ASM9);  // bytecode version supported by asm
        this.callSites = callSites;
        this.internalClasses = internalClasses;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.currentClass = name.replace('/', '.');  // convert to source code syntax for better readability
        internalClasses.add(name);
        if (signature != null) {
            SignatureReader signatureReader = new SignatureReader(signature);
            signatureReader.accept(new SignatureCallSiteVisitor(callSites, internalClasses));
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        excludeFirstCharacter(descriptor, callSites);
        if (cv != null) {
            return cv.visitAnnotation(descriptor, visible);
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        this.currentMethod = this.currentClass + "::" + name + descriptor;
        if (exceptions != null) {
            Collections.addAll(callSites, exceptions);
        }
        if (signature != null) {
            SignatureReader signatureReader = new SignatureReader(signature);
            signatureReader.accept(new SignatureCallSiteVisitor(callSites, internalClasses));
        }

        Type methodType = Type.getMethodType(descriptor);
        for (Type paramType: methodType.getArgumentTypes()) {
            if (notPrimitive(paramType.toString())) {
                excludeFirstCharacter(paramType.toString(), callSites);
            }
        }

        Type returnType = methodType.getReturnType();
        if (!"V".equals(returnType.toString()) && notPrimitive(returnType.toString())) {
            excludeFirstCharacter(returnType.toString(), callSites);
        }

        return new MethodVisitor(Opcodes.ASM9) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                // check if owner is a primitive array
                if (notPrimitive(owner)) {
                    String callee = owner + "::" + name + "::" +descriptor;
                    excludeFirstCharacter(callee, callSites);
                }
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {

                if (notPrimitive(owner)) {
                    String callee = owner + ":::" + name + ":::" +descriptor;
                    excludeFirstCharacter(callee, callSites);
                }
            }

            // have to specifically track the override annotation
            @Override
            public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
                excludeFirstCharacter(descriptor, callSites);
                if (mv != null) {
                    return mv.visitAnnotation(descriptor, visible);
                }
                return null;
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation(
                    final int parameter, final String descriptor, final boolean visible) {
                excludeFirstCharacter(descriptor, callSites);
                if (mv != null) {
                    return mv.visitParameterAnnotation(parameter, descriptor, visible);
                }
                return null;
            }

            @Override
            public void visitTryCatchBlock(
                    final Label start, final Label end, final Label handler, final String type) {
                excludeFirstCharacter(type, callSites);
                if (mv != null) {
                    mv.visitTryCatchBlock(start, end, handler, type);
                }
            }

            @Override
            public AnnotationVisitor visitTryCatchAnnotation(
                    final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
                excludeFirstCharacter(descriptor, callSites);
                if (mv != null) {
                    return mv.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
                }
                return null;
            }

        };
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (notPrimitive(descriptor)) {
            excludeFirstCharacter(descriptor, callSites);
        }
        if (signature != null) {
            SignatureReader signatureReader = new SignatureReader(signature);
            signatureReader.accept(new SignatureCallSiteVisitor(callSites, internalClasses));
        }
        return new FieldVisitor(Opcodes.ASM9) {

            @Override
            public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
                excludeFirstCharacter(descriptor, callSites);
                if (fv != null) {
                    return fv.visitAnnotation(descriptor, visible);
                }
                return null;
            }

        };
    }



}
