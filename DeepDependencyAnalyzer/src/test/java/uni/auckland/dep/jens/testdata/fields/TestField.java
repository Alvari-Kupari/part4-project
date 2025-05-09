package uni.auckland.dep.jens.testdata.fields;

import uni.auckland.dep.jens.testdata.Demo;
import uni.auckland.dep.jens.testdata.Foo;

import java.util.ArrayList;
import java.util.List;

public class TestField {

    private static final String CONSTANT_1 = "Constant1";

    int i1;
    byte bb1;
    List<Demo> demos = new ArrayList<>();

    void testMethod() {
        boolean flag = true;
        String strVal = Foo.test;
        Foo.testInt = 10;
        Foo foo = new Foo();
        Float fValue = foo.testFloat;
        foo.testBool = false;
        System.out.println(strVal + " " + fValue);
    }
}
