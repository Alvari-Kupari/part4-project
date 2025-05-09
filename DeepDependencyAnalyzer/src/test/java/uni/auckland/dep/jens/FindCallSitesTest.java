package uni.auckland.dep.jens;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindCallSitesTest {

    @Test
    public void testMethodAnnotationCallSites() throws IOException {

        boolean flag =  Pattern.matches("[\\[][L]", "[L");

        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/methodannotation");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);
        Set<String> externalCallSites = internalClassesAndCallSites.get(FindCallSites.EXTERNAL);
        assertEquals(8,externalCallSites.size());
        assertTrue(externalCallSites.contains("edu/umd/cs/findbugs/annotations/NonNull;"));
        assertTrue(externalCallSites.contains("javax/persistence/Column;"));
        assertTrue(externalCallSites.contains("javax/annotation/Nonnull;"));
    }

    @Test
    public void testFieldAnnotationCallSites() throws IOException {
        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/fieldannotation");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);
        Set<String> externalCallSites = internalClassesAndCallSites.get(FindCallSites.EXTERNAL);
        assertEquals(7, externalCallSites.size());
        assertTrue(externalCallSites.contains("edu/umd/cs/findbugs/annotations/NonNull;"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/Demo;"));
        assertEquals("uni/auckland/dep/jens/testdata/fieldannotation/FieldAnnotations", internalClassesAndCallSites.get(FindCallSites.INTERNAL).iterator().next());
    }

    @Test
    public void testClassAnnotationCallSites() throws IOException {
        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/classannotation");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);
        Set<String> externalCallSites = internalClassesAndCallSites.get(FindCallSites.EXTERNAL);
        assertEquals(5, externalCallSites.size());
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/PersonalData;"));
        assertTrue(externalCallSites.contains("org/junit/jupiter/api/Disabled;"));
        Set<String> internalCallSites = internalClassesAndCallSites.get(FindCallSites.INTERNAL);
        assertTrue(internalCallSites.contains("uni/auckland/dep/jens/testdata/classannotation/InterfaceAnnotations"));
        assertTrue(internalCallSites.contains("uni/auckland/dep/jens/testdata/classannotation/ClassAnnotations"));
    }

    @Test
    public void testSuperClassCallSites() throws IOException {
        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/superclasses");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);
        Set<String> externalCallSites = internalClassesAndCallSites.get(FindCallSites.EXTERNAL);
        assertEquals(10, externalCallSites.size());
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/TestInterface"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/SuperClass"));
        assertTrue(externalCallSites.contains("java/lang/annotation/Annotation"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/PersonalData"));
        Set<String> internalCallSites = internalClassesAndCallSites.get(FindCallSites.INTERNAL);
        assertTrue(internalCallSites.contains("uni/auckland/dep/jens/testdata/superclasses/ClassWithNoSuper"));
        assertTrue(internalCallSites.contains("uni/auckland/dep/jens/testdata/superclasses/ChildClass"));
    }

    @Test
    public void testFieldCallSites() throws IOException {
        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/fields");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);
        Set<String> externalCallSites = internalClassesAndCallSites.get(FindCallSites.EXTERNAL);
        assertEquals(20, externalCallSites.size());
        assertTrue(externalCallSites.contains("java/lang/String;"));
        assertTrue(externalCallSites.contains("java/lang/Boolean::valueOf::(Z)Ljava/lang/Boolean;"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/Demo"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/Foo:::test:::Ljava/lang/String;"));
        Set<String> internalCallSites = internalClassesAndCallSites.get(FindCallSites.INTERNAL);
        assertTrue(internalCallSites.contains("uni/auckland/dep/jens/testdata/fields/TestField"));
    }

    @Test
    public void testMethodCallSites() throws IOException {
        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/methods");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);
        Set<String> externalCallSites = internalClassesAndCallSites.get(FindCallSites.EXTERNAL);
        assertEquals(28, externalCallSites.size());
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/CustomException"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/Foo::bar::()V"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/Demo"));
        assertTrue(externalCallSites.contains("uni/auckland/dep/jens/testdata/Foo;"));
        assertTrue(externalCallSites.contains("java/io/FileNotFoundException"));
        Set<String> internalCallSites = internalClassesAndCallSites.get(FindCallSites.INTERNAL);
        assertTrue(internalCallSites.contains("uni/auckland/dep/jens/testdata/methods/TestMethod"));
    }

    @Test
    public void testMethodExceptionsCallSites() throws IOException {
        File classes = new File("target/test-classes/uni/auckland/dep/jens/testdata/runtimeexception");
        Assumptions.assumeTrue(classes.exists());
        Map<String, Set<String>> internalClassesAndCallSites = FindCallSites.findCallSites(classes);

    }
}
