package uni.auckland.dep.jens.testdata.methodannotation;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import java.util.zip.ZipFile;

public class MethodAnnotations {

    public @Nonnull String returnParamAnnotation() {
        return "return annotation";
    }

    @Deprecated
    public void deprecatedMethod() {}

    @Column(name = "column 1")
    public void annotationWithValue(){}

    public void annotationInParam(@NonNull ZipFile zipFile){}
}
