package uni.auckland.dep.jens.testdata.methodoverriding;

import uni.auckland.dep.jens.testdata.OverriddenSuperClass;

import javax.annotation.Nonnull;
import java.io.IOException;

public class MethodOverriding extends OverriddenSuperClass {

    @Override
    public void overrideMethod() {
        System.out.println("Override");
    }

}
