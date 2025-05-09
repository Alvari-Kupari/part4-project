package uni.auckland.dep.jens;

import java.util.Set;
import java.util.regex.Pattern;

public class DataPreProcessing {
    protected static void excludeFirstCharacter(String descriptor, Set<String> callSites) {
        if (descriptor != null) {
            descriptor = removeArrayBrackets(descriptor);
            if (descriptor.startsWith("L")) { // if a reference value
                callSites.add(descriptor.substring(1));
            } else {
                callSites.add(descriptor);
            }
        }
    }

    private static String removeArrayBrackets(String descriptor) {
        String bracketRemoved = descriptor;
        if (descriptor.startsWith("[")) {
            bracketRemoved = descriptor.substring(1);
        }
        if (bracketRemoved.startsWith("[")) {
            removeArrayBrackets(bracketRemoved);
        }
        return bracketRemoved;
    }

    protected static boolean notPrimitive(String descriptor) {
        return !Pattern.matches("[\\[]*[BCDFIJSZ]", descriptor);
    }
}
