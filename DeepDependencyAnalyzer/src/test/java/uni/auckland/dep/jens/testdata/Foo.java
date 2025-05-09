package uni.auckland.dep.jens.testdata;

public class Foo {

    public static String test = "test1";
    public static Integer testInt;
    public Float testFloat = 10.0F;
    public Boolean testBool;
    public
    static void foo() {
        bar();
        bar(42);
        bar(1);
    }
    public static void bar() {

    }

    static  Integer bar(int i) {
        return i;
    }
}
