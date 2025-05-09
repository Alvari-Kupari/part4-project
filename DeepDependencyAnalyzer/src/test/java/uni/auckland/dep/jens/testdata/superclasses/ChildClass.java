package uni.auckland.dep.jens.testdata.superclasses;

import uni.auckland.dep.jens.testdata.PersonalData;
import uni.auckland.dep.jens.testdata.SuperClass;
import uni.auckland.dep.jens.testdata.TestInterface;

import java.lang.annotation.Annotation;

public class ChildClass extends SuperClass implements TestInterface, PersonalData {
    @Override
    public String name() {
        return null;
    }

    @Override
    public int age() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
