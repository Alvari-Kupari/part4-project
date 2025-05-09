package uni.auckland.dep.jens.testdata.classannotation;

import uni.auckland.dep.jens.testdata.PersonalData;

@PersonalData(name = "John", age = 11)
public class ClassAnnotations {
    @Deprecated
    class InnerAnnotation{}
}
