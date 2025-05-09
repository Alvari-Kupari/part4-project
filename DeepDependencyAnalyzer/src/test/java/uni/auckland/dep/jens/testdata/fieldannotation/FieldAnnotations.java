package uni.auckland.dep.jens.testdata.fieldannotation;

import edu.umd.cs.findbugs.annotations.NonNull;
import uni.auckland.dep.jens.testdata.Demo;

public class FieldAnnotations {
    private @NonNull Integer age = 8;
    @Deprecated
    public Demo demo;
}
