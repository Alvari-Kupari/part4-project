package uni.auckland.dep.jens.testdata.methods;

import org.apache.commons.collections4.FunctorException;
import uni.auckland.dep.jens.testdata.CustomException;
import uni.auckland.dep.jens.testdata.Demo;
import uni.auckland.dep.jens.testdata.Foo;
import uni.auckland.dep.jens.testdata.SuperClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import static java.lang.reflect.Array.getInt;

public class TestMethod<R extends File> {

    // With parameter and No return type
    R testFooParam(Foo foo, Integer ii) {
        return (R)new File("dd");
    }

    // With parameter and No return type
    Boolean testParamAndReturn(String testS) {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.txt");
        } catch (FileNotFoundException e) {
            //
        }
        return false;

    }

    // FunctorException is from the org.apache.commons:commons-collection which is a deep dependency
    int testDeepException() throws FunctorException {
        return 1;
    }

    // test to get generic values of parameter
    void testGenerics1(List<Demo> demoValues) {
    }

    // test to get generic values of return type
    ArrayList<SuperClass> testGenerics2() {
        return new ArrayList<SuperClass>();
    }

    void testMethod1() throws CustomException {

        int [] a = {2,4,6};
        int []b = {3};
        getInt(a, 0);

        a.clone();

        Foo.bar();
        throw new CustomException("Error");
    }

    private String testMethod2 () throws IOException {
        File f = new File("test.txt");
        JarFile jarFile = new JarFile(f);
        return "test";
    }
}
