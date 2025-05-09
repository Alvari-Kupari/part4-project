package uni.auckland.dep.jens;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.Set;

import static uni.auckland.dep.jens.DataPreProcessing.excludeFirstCharacter;
import static uni.auckland.dep.jens.DataPreProcessing.notPrimitive;

public class SignatureCallSiteVisitor extends SignatureVisitor {
    private Set<String> callSites;
    private Set<String> internalClasses;

    protected SignatureCallSiteVisitor(Set<String> callSites, Set<String> internalClasses) {
        super(Opcodes.ASM9);
        this.callSites = callSites;
        this.internalClasses = internalClasses;
    }

    @Override
    public void visitFormalTypeParameter(final String name) {
        // check if it is a generic type to add as internal calls

        if (notPrimitive(name)) {
            if (!name.contains("/")) {
                internalClasses.add(name);
            } else {
                excludeFirstCharacter(name, callSites);
            }
        }
    }


    @Override
    public void visitClassType(final String name) {
        if (notPrimitive(name)) {
            excludeFirstCharacter(name, callSites);
        }
    }

    @Override
    public void visitInnerClassType(final String name) {
        if (notPrimitive(name)) {
            excludeFirstCharacter(name, callSites);
        }
    }

}
