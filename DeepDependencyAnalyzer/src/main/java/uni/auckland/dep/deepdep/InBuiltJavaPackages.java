package uni.auckland.dep.deepdep;

import java.util.HashSet;
import java.util.Set;

public class InBuiltJavaPackages {

    // create a list of java packages you want to avoid as external calls
    public static Set<String> getJavaInBuiltPackages() {
        Set<String> packages = new HashSet<>();

        packages.add("com/oracle");
        packages.add("com/sun/");
        packages.add("java/");
        packages.add("javafx/");
        packages.add("javax/");
        packages.add("jdk/dynalink/");
        packages.add("jdk/incubator/");
        packages.add("jdk/javadoc/doclet/");
        packages.add("jdk/jfr/");
        packages.add("jdk/jshell/");
        packages.add("jdk/management/jfr/");
        packages.add("jdk/nashorn/");
        packages.add("jdk/net/");
        packages.add("jdk/nio/");
        packages.add("jdk/security/jarsigner/");
        packages.add("netscape/javascript/");
        packages.add("org/ietf/jgss/");
        packages.add("org/omg/");
        packages.add("org/w3c/dom/");
        packages.add("org/xml/sax/");
        packages.add("sun/");

        return packages;
    }
}
