package uni.auckland.dep.jens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Analysis to find callsites. Can be used for callgraph construction.
 *
 * @author jens dietrich
 */
public class FindCallSites {

    public static final String EXTERNAL = "External";
    public static final String INTERNAL = "Internal";
    private static final Logger logger = LogManager.getLogger(FindCallSites.class);

    public static Map<String, Set<String>> findCallSites(File folderWithClassFiles) throws IOException {
        // collect class files
        List<File> classFiles = Files.walk(folderWithClassFiles.toPath())
                .map(Path::toFile)
                .filter(f -> f.getName().endsWith(".class"))
                .collect(Collectors.toList());

        // the actual analysis
        Set<String> callSites = new HashSet<>();
        Set<String> internalClasses = new HashSet<>();
        Set<String> externalCallSites = new HashSet<>();
        Map<String, Set<String>> callSitesAndInternalClasses = new HashMap<>();
        for (File classFile : classFiles) {
            try (InputStream in = new FileInputStream(classFile)) {
                ClassReader classReader = new ClassReader(in);
                // added super classes as call sites
                try {
                    callSites.add(classReader.getSuperName());
                    callSites.addAll(Arrays.asList(classReader.getInterfaces()));
                } catch (RuntimeException e) {
                    logger.error("Error occurred while getting super types for class");
                }

                classReader.accept(new RecordCallSitesVisitor(callSites, internalClasses), 0);
            }
        }
        callSitesAndInternalClasses.put(INTERNAL, internalClasses);
        // filter internal method calls
        for (String callee : callSites) {
            AtomicBoolean externalCall = new AtomicBoolean(true);
            internalClasses.forEach(classFile -> {
                if (callee != null) {
                    if (callee.startsWith((classFile))) {
                        externalCall.set(false);
                    } else if (callee.startsWith("[L") && callee.startsWith("[L" + classFile)) {
                        externalCall.set(false);
                    }
                }
            });
            // only add as external call if it does not belong to an internal method
            if (externalCall.get()) {
                externalCallSites.add(callee);
            }
        }
        callSitesAndInternalClasses.put(EXTERNAL, externalCallSites);
        return callSitesAndInternalClasses;
    }

}
