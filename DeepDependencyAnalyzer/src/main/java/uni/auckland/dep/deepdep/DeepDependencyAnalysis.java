package uni.auckland.dep.deepdep;

import com.opencsv.CSVWriter;
import fr.dutra.tools.maven.deptree.core.InputType;
import fr.dutra.tools.maven.deptree.core.Node;
import fr.dutra.tools.maven.deptree.core.ParseException;
import fr.dutra.tools.maven.deptree.core.Parser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uni.auckland.dep.db.entity.MvnProjectDetailsNotransitive;
import uni.auckland.dep.db.query.MVNProjectQuery;
import uni.auckland.dep.jens.FindCallSites;
import uni.auckland.dep.model.DependencyModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DeepDependencyAnalysis {

    private static final Logger logger = LogManager.getLogger(DeepDependencyAnalysis.class);
    // For java 8
    //private static final String COPIED_DEPENDENCIES_DIRECTORY = "../../CopiedDependencies";
    //private static final String COPY_DEPENDENCY_TREE = "../../CopyDependencyTree/DependencyTree.txt";
    //private final static String MVN_PROJECT_DIRECTORY = "../../GitRepository/MavenDependentProjectRepo/";
    private final static String POM_FILE = "pom.xml";
    private final static String META_INF_FILE = "META-INF";
   // private static final String SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_CSV = "src/main/resources/DeepDependencyResults25.csv";
   // private static final String SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_XLSX = "src/main/resources/DeepDependencyResults25.xlsx";

    // For java 11
    private static final String COPIED_DEPENDENCIES_DIRECTORY = "../../../CopiedDependenciesJava11";
    private static final String COPY_DEPENDENCY_TREE = "../../../CopyDependencyTreeJava11/DependencyTree.txt";
    private final static String MVN_PROJECT_DIRECTORY = "../../../GitRepository/MavenDependentProjectRepo/";
    private static final String SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_CSV = "../src/main/resources/DeepDependencyResultsJava11_25.csv";
    private static final String SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_XLSX = "../src/main/resources/DeepDependencyResultsJava11_25.xlsx";
    private static final String TARGET = "/target";
    private static final String CLASSES = "/classes";
    private static final String TEST_CLASSES = "/test-classes";
    public static final String DEPENDENCY_CLASSES = "DependencyClasses";
    public static final String EXTERNAL_FILES = "ExternalDepFiles";
    public static final String NOT_MAPPED = "NotMapped";

    // draw dependency tree
    // collect all dependencies
    // map external dependencies to which level
    public static void main(String[] args) throws IOException {
        DeepDependencyAnalysis deepDependencyAnalysis = new DeepDependencyAnalysis();
        deepDependencyAnalysis.extractDeepDependencies();
    }

    private void extractDeepDependencies() throws IOException {
        MVNProjectQuery mvnProjectQuery = new MVNProjectQuery();
        Set<String> inBuiltJavaClasses = InBuiltJavaPackages.getJavaInBuiltPackages();
        ArrayList<MvnProjectDetailsNotransitive> projectDetails = mvnProjectQuery.getMVNProjects();
        for (MvnProjectDetailsNotransitive projectDetail : projectDetails) {
            String directoryName = projectDetail.getDirectory();
            String projectDirectory = projectDetail.getFolderName();
            logger.info("Processing project " + projectDetail.getDirectory());
            // draw dependency tree
            List<DependencyModel> dependencyOrder = new ArrayList<>();
            Map<Integer, List<DependencyModel>> dependencyLevels = new LinkedHashMap <>();
            Node dependencyTree = getDependencyGraph(directoryName, dependencyOrder, dependencyLevels);

            if (dependencyTree != null && dependencyLevels.size() > 0 ) {
                // copy dependencies to folder
                if (copyProjectDependencies(directoryName)) {
                    // get java classes in the entire repository
                    Set<String> javaClassesInRepo = findJavaClassesInDirectory(MVN_PROJECT_DIRECTORY + projectDirectory);
                    // get classes in dependencies jar files
                    File dependencyDirectory = new File(COPIED_DEPENDENCIES_DIRECTORY);
                    Map<DependencyModel, Set<String>> dependencyClasses = new LinkedHashMap<>();
                    Set<String> externalFiles = getDependencyClasses(dependencyDirectory, dependencyClasses, dependencyOrder);

                    // build client project
                    if (buildClientProject(directoryName)) {
                        String projectTargetDirectory = MVN_PROJECT_DIRECTORY + directoryName.replace("\\", "/") + TARGET;
                        // check if target exists and class and test-classes
                        File targetFolder = new File(projectTargetDirectory);
                        if (targetFolder.exists()) {
                            // get classes in target folder
                            File classesDirectory = new File(projectTargetDirectory + CLASSES);
                            File testClassesDirectory = new File(projectTargetDirectory + TEST_CLASSES);
                            Map<String, Set<String>> classesCallSites = getCallSitesToVerify(classesDirectory);
                           // classesCallSites.entrySet().forEach(entrey -> {entrey.getValue().forEach(val -> logger.info(val));});
                            Map<String, Set<String>> testClassesCallSites = getCallSitesToVerify(testClassesDirectory);
                            // get external call sites
                            Map<Integer, Map<String, List<String>>> deepDependencyCallSites = filterDeepDependencyCallSites(classesCallSites,
                                    testClassesCallSites, dependencyClasses, javaClassesInRepo, inBuiltJavaClasses, dependencyLevels);
                            writeExternalCallsToCSV(deepDependencyCallSites, projectDetail, dependencyLevels, externalFiles);
                            writeExternalCallsToExcel(deepDependencyCallSites, projectDetail, dependencyLevels, externalFiles);
                        }
                    }
                    // delete jars collected
                    deleteFilesInDirectory(dependencyDirectory);
                    deleteFilesInDirectory(new File(COPY_DEPENDENCY_TREE));
                    logger.info("Finished processing project " + projectDetail.getDirectory());
                }
            }
        }
    }

    /**
     * Get all dependency classes in the collected dependency folder
     *
     * @param dependencyDirectory the directory in which all dependencies were copied to
     * @return all the classes in dependencies and any other external files
     * @throws IOException throw exception if jar file is not found
     */
    private Set<String> getDependencyClasses(File dependencyDirectory, Map<DependencyModel, Set<String>> dependencyClasses, List<DependencyModel> dependencyOrder) throws IOException {
        File[] depFiles = dependencyDirectory.listFiles();
        Set<String> externalFiles = new HashSet<>();
        if (depFiles != null) {
            for (File depFile : depFiles) {
                if (depFile.getName().endsWith(".jar")) {
                    // map the jar file with the dependency name
                    //TODO: if dependency map is not found would get null pointer exception have to find the exact name in the jar
                    Optional<DependencyModel> dependencyModelOptional = dependencyOrder.stream()
                            .filter(dep -> ((dep.getClassifier() == null &&
                                    depFile.getName().equals(dep.getArtifactId() + "-" + dep.getVersion() + ".jar")) ||
                                    (dep.getClassifier() != null && depFile.getName().equals(dep.getArtifactId() +
                                            "-" + dep.getVersion() + "-" + dep.getClassifier() + ".jar")))).findFirst();
                    if (dependencyModelOptional.isPresent()) {
                        DependencyModel dependency = dependencyModelOptional.get();
                        dependencyClasses.put(dependency, getJavaClassNamesFromCompressedFiles(depFile));
                    } else {
                        System.out.println("Not found " + depFile.getName());
                        externalFiles.add(depFile.getName());
                    }
                } else {
                    externalFiles.add(depFile.getName());
                }
            }
        }
        return externalFiles;
    }

    /**
     * Get deep dependency call sites after filtering out the classes in the repository and dependencies
     *
     * @param classesCallSites      call sites extracted for classes in the target classes folder
     * @param testClassesCallSites  call sites extracted for classes in the target test classes folder
     * @param dependencyClasses     classes under all direct dependencies
     * @param javaClassesInRepo     all classes in th entire repository
     * @param inBuiltJavaClasses    in built java packages to avoid
     * @param dependencyLevels      dependencies with the dependency level they are linked to the project
     * @return Map with the dependency level and dependency and its call site details
     *
     */
    private Map<Integer, Map<String, List<String>>> filterDeepDependencyCallSites(Map<String, Set<String>> classesCallSites, Map<String,
            Set<String>> testClassesCallSites, Map<DependencyModel, Set<String>> dependencyClasses, Set<String> javaClassesInRepo,
                                                      Set<String> inBuiltJavaClasses, Map<Integer, List<DependencyModel>> dependencyLevels) {
        Set<String> externalCallSites = new HashSet<>();
        Set<String> mainInternalClasses = null;
        Set<String> testInternalClasses = null;
        // exclude internal call sites
        if (classesCallSites.size() > 0) {
            mainInternalClasses = classesCallSites.get(FindCallSites.INTERNAL);
            externalCallSites.addAll(classesCallSites.get(FindCallSites.EXTERNAL));
        }
        if (testClassesCallSites.size() > 0) {
            testInternalClasses = testClassesCallSites.get(FindCallSites.INTERNAL);
            externalCallSites.addAll(testClassesCallSites.get(FindCallSites.EXTERNAL));
        }

        excludeInternalCallSites(externalCallSites, testInternalClasses);
        excludeInternalCallSites(externalCallSites, mainInternalClasses);
        excludeInternalCallSites(externalCallSites, inBuiltJavaClasses);
        excludeInternalCallSites(externalCallSites, javaClassesInRepo);

        Map<Integer, Map<String, List<String>>> externalCalls = getDeepDependencyCalls(dependencyClasses, dependencyLevels, externalCallSites);

        if (externalCalls.size() > 0) {
            System.out.println("Contains External call sites");
        }
        return externalCalls;
    }

    private Map<Integer, Map<String, List<String>>> getDeepDependencyCalls(Map<DependencyModel, Set<String>> dependencyClasses, Map<Integer, List<DependencyModel>> dependencyLevels, Set<String> externalCallSites) {
        Map<Integer, Map<String, List<String>>> externalCalls = new LinkedHashMap<>();
        // grade the call sites based on the level
        if (dependencyLevels.size() > 1) {
            for (Map.Entry<Integer, List<DependencyModel>> entry: dependencyLevels.entrySet()) {
                Integer level = entry.getKey();
                List<DependencyModel> dependencyModels = entry.getValue();
                Map<String, List<String>> externalCallsFromDependency = new LinkedHashMap<>();
                dependencyModels.forEach(dependency -> {
                    if (dependencyClasses.containsKey(dependency)) {
                        List<String> callSitesForDep = new ArrayList<>();
                        for (Iterator<String> iterator = externalCallSites.iterator(); iterator.hasNext();) {
                            String callSite = iterator.next();
                            for (Iterator<String> internalIterator = dependencyClasses.get(dependency).iterator(); internalIterator.hasNext();) {
                                String depClasses = internalIterator.next();
                                if (callSite != null && callSite.startsWith(depClasses)) {
                                    callSitesForDep.add(callSite);
                                    iterator.remove();
                                    break;
                                } else if (callSite != null && callSite.startsWith("[L") && callSite.startsWith("[L" + depClasses)) {
                                    callSitesForDep.add(callSite);
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                        if (callSitesForDep.size() > 0) {
                            externalCallsFromDependency.put((dependency.getDependencyName() + " " + dependency.getVersion()), callSitesForDep);
                        }

                    }
                });
                externalCalls.put(level, externalCallsFromDependency);
            }
            if (externalCallSites.size() > 0) {
                Map<String, List<String>> externalCallsNotMapped = new HashMap<>();
                externalCallsNotMapped.put(NOT_MAPPED, new ArrayList<>(externalCallSites));
                externalCalls.put(0, externalCallsNotMapped);
            }
        }
        return externalCalls;
    }

    /**
     * Exclude the internal classes from the class sites
     *
     * @param callSites     all call sites which needs to be filtered
     * @param internalClasses   internal classes to be filtered out
     */
    private void excludeInternalCallSites(Set<String> callSites, Set<String> internalClasses) {
        if (callSites != null) {
            for (Iterator<String> iterator = callSites.iterator(); iterator.hasNext();) {
                String callSite = iterator.next();
                if (internalClasses != null) {
                    for (Iterator<String> internalIterator = internalClasses.iterator(); internalIterator.hasNext();) {
                        String classFile = internalIterator.next();
                        if (callSite != null && callSite.startsWith(classFile)) {
                            iterator.remove();
                            break;
                        } else if (callSite != null && callSite.startsWith("[L") && callSite.startsWith("[L" + classFile)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void writeExternalCallsToCSV(Map<Integer, Map<String, List<String>>> deepDependencyCallSites,
                                         MvnProjectDetailsNotransitive projectDetail, Map<Integer,
            List<DependencyModel>> dependencyLevels, Set<String> externalFiles){
        String directDependencies = null;
        int directDependencyCount = 0;
        if (dependencyLevels.containsKey(1)) {
            directDependencies = dependencyLevels.get(1).stream().map(DependencyModel::getDependencyName).collect(Collectors.joining("\n"));
            directDependencyCount = dependencyLevels.get(1).size();
        }
        String externalDepFiles = String.join("\n", externalFiles);
        File file = new File(SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_CSV);
        try {
            FileWriter outputfile = new FileWriter(file, true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
            List<String> dataToInsert = new ArrayList<>();
            dataToInsert.add(Integer.toString(projectDetail.getMvnProjectDetailNotransitiveId()));
            dataToInsert.add(projectDetail.getProjectName());
            dataToInsert.add(projectDetail.getDirectory());
            dataToInsert.add("11");
            dataToInsert.add(Integer.toString(directDependencyCount));
            dataToInsert.add(directDependencies);
            dataToInsert.add(projectDetail.getRepositoryUrl());
            dataToInsert.add(externalDepFiles);
            if (deepDependencyCallSites.size() > 0) {
                if (deepDependencyCallSites.containsKey(0)) {
                    String unmappedDep = String.join("\n", deepDependencyCallSites.get(0).get(NOT_MAPPED));
                    dataToInsert.add(unmappedDep);
                } else {
                    dataToInsert.add(StringUtils.EMPTY);
                }
                for (int level = 1; level <= 11; level++) {
                    if (deepDependencyCallSites.containsKey(level)) {
                        Map<String, List<String>>  dependenciesWithCallSites = deepDependencyCallSites.get(level);
                        StringBuilder callSitesWithDep = new StringBuilder();
                        for (Map.Entry<String, List<String>> depEntry: dependenciesWithCallSites.entrySet()) {
                            if (callSitesWithDep.length() > 0 ){
                                callSitesWithDep.append("\n");
                            }
                            callSitesWithDep.append(depEntry.getKey()).append(" -> \n").append(String.join("\n", depEntry.getValue()));
                        }
                        dataToInsert.add(Integer.toString(dependenciesWithCallSites.size()));
                        dataToInsert.add(callSitesWithDep.toString());
                    } else {
                        dataToInsert.add("0");
                        dataToInsert.add(StringUtils.EMPTY);
                    }
                }
            }

            // add data to csv
            String[] data1 = dataToInsert.toArray(new String[dataToInsert.size()]);
            writer.writeNext(data1);

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            logger.error("Error while writing to csv for XXX");
        }
    }
    /**
     * Write all external call sites to xlsx file
     *
     * @param deepDependencyCallSites map of deep dependency level and dependency with its call sites referred
     * @param projectDetail           DB record of the maven project details
     * @param dependencyLevels        Dependencies in order they are referred by the project
     * @param externalFiles           External files copied but not matched with the dependency tree
     */
    private void writeExternalCallsToExcel(Map<Integer, Map<String, List<String>>> deepDependencyCallSites,
                                           MvnProjectDetailsNotransitive projectDetail, Map<Integer,
            List<DependencyModel>> dependencyLevels, Set<String> externalFiles) {

        String directDependencies = null;
        int totalDirectDependencies = 0;
        if (dependencyLevels.containsKey(1)) {
            directDependencies = dependencyLevels.get(1).stream().map(DependencyModel::getDependencyName).collect(Collectors.joining("\n"));
            totalDirectDependencies = dependencyLevels.get(1).size();
        }
        String externalDepFiles = String.join("\n", externalFiles);
        try {
            FileInputStream fileInputStream = new FileInputStream(SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_XLSX);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("Sheet1");

            int lastRow = xssfSheet.getLastRowNum();
            XSSFRow row = xssfSheet.createRow(lastRow + 1);
            XSSFCell cell = row.createCell(0);
            row.createCell(0).setCellValue(projectDetail.getMvnProjectDetailNotransitiveId());
            row.createCell(1).setCellValue(projectDetail.getProjectName());
            row.createCell(2).setCellValue(projectDetail.getDirectory());
            row.createCell(3).setCellValue("11"); // build Java version
            row.createCell(4).setCellValue(totalDirectDependencies);
            row.createCell(5).setCellValue(directDependencies);
            row.createCell(6).setCellValue(projectDetail.getRepositoryUrl());
            row.createCell(7).setCellValue(externalDepFiles);
            if (deepDependencyCallSites.size() > 0) {
                if (deepDependencyCallSites.containsKey(0)) {
                    String unmappedDep = String.join("\n", deepDependencyCallSites.get(0).get(NOT_MAPPED));
                    if (unmappedDep.length() < 32767) {
                        row.createCell(8).setCellValue(unmappedDep);
                    } else{
                        row.createCell(8).setCellValue(unmappedDep.substring(0, 32766));
                    }
                }
                int splitColumn = 0;
                for (int level = 1; level <= 11; level++) {
                    if (deepDependencyCallSites.containsKey(level)) {
                        Map<String, List<String>>  dependenciesWithCallSites = deepDependencyCallSites.get(level);
                        StringBuilder callSitesWithDep = new StringBuilder();
                        for (Map.Entry<String, List<String>> depEntry: dependenciesWithCallSites.entrySet()) {
                            if (callSitesWithDep.length() > 0 ){
                                callSitesWithDep.append("\n");
                            }
                            callSitesWithDep.append(depEntry.getKey()).append(" -> \n").append(String.join("\n", depEntry.getValue()));
                        }
                        row.createCell(8 + ((level*2)-1)).setCellValue(Integer.toString(dependenciesWithCallSites.size()));
                        if (callSitesWithDep.length() < 32767) {
                            row.createCell(8 + (level*2)).setCellValue(callSitesWithDep.toString());
                        } else{
                            // if more than 32766 add to cell after 10th column with necessary details for tracking
                            row.createCell(8 + (level*2)).setCellValue(callSitesWithDep.substring(0, 32766));
                            splitColumn = getSplitColumn(row, splitColumn, level, callSitesWithDep.substring(32766));
                        }
                    } else {
                        row.createCell(8 + ((level*2)-1)).setCellValue("0");
                        row.createCell(8 + (level*2)).setCellValue(StringUtils.EMPTY);
                    }
                }
            }

            FileOutputStream outputStream = new FileOutputStream(SRC_MAIN_RESOURCES_DEEP_DEPENDENCY_XLSX);
            xssfWorkbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getSplitColumn(XSSFRow row, int splitColumn, int level, String callSitesWithDep) {
        String dependencyLevel = "DeepDependencyLevel" + (level -1) + "\n";
        if ((dependencyLevel + callSitesWithDep).length() < 32767 ) {
            row.createCell(31 + splitColumn).setCellValue(dependencyLevel + callSitesWithDep);
            splitColumn++;
        } else{
            String subString = dependencyLevel + callSitesWithDep;
            row.createCell(31 + splitColumn).setCellValue(subString.substring(0, 32766));
            splitColumn += 1;
            splitColumn = getSplitColumn(row, splitColumn, level, subString.substring(32766));

           // row.createCell(32 + splitColumn).setCellValue(dependencyLevel + subString.substring(32766));

        }
        return splitColumn;
    }

    /**
     * Get call sites of project classes which needs to be verified with if they are external dependency calls
     * @param classesDirectory directory path to get the class files
     * @return                 A map containing internal and external call sites used in the project
     * @throws IOException     An exception will occur if the find call site method returns an exception while process
     */
    private Map<String, Set<String>> getCallSitesToVerify(File classesDirectory) throws IOException {
        Map<String, Set<String>> callSitesAndInternalClasses = new HashMap<>();
        if (classesDirectory.exists()) {
            File[] classDirectories = classesDirectory.listFiles(File::isDirectory);
            for (File projectClassesDir : classDirectories) {
                if (!META_INF_FILE.equals(projectClassesDir.getName())) {
                    Map<String, Set<String>> callSitesAndIntClassesForDir = FindCallSites.findCallSites(projectClassesDir);
                    if (callSitesAndInternalClasses.containsKey(FindCallSites.INTERNAL)) {
                        callSitesAndInternalClasses.get(FindCallSites.INTERNAL).addAll(callSitesAndIntClassesForDir.get(FindCallSites.INTERNAL));
                        callSitesAndInternalClasses.get(FindCallSites.EXTERNAL).addAll(callSitesAndIntClassesForDir.get(FindCallSites.EXTERNAL));
                    } else {
                        callSitesAndInternalClasses = callSitesAndIntClassesForDir;
                    }
                }
            }
        }
        return callSitesAndInternalClasses;
    }

    /**
     * Build the client project without tests to generate the class files for the analysis
     *
     * @param directoryName       the local directory which contains the project
     * @return if the project was successfully built or not
     * @throws IOException
     */
    private boolean buildClientProject(String directoryName) throws IOException {
        boolean projectBuildSuccess = false;
        String pomFile = MVN_PROJECT_DIRECTORY + directoryName.replace("\\", "/") + "/" + POM_FILE;
        String mvnCmd = String.format("mvn.cmd -f %s clean install -DskipTests", pomFile);
        System.out.println(mvnCmd);
        ArrayList<String> copyOutput = runCommandWithExecutor(mvnCmd);
        for (String output : copyOutput) {
            if (output != null && output.contains("BUILD SUCCESS")) {
                projectBuildSuccess = true;
                logger.info("Successfully Built project  " + directoryName);
            }
        }
        return projectBuildSuccess;
    }

    private Node getDependencyGraph(String directoryName, List<DependencyModel> dependencyOrder, Map<Integer,
            List<DependencyModel>> dependencyLevels) throws IOException {
        Node tree = null;

        String depTreeFolder = System.getProperty("user.dir").replace("\\", "/") + "/"
                + COPY_DEPENDENCY_TREE;

        String pomFile = MVN_PROJECT_DIRECTORY + directoryName.replace("\\", "/") + "/" + POM_FILE;
        String mvnCmd = String.format("mvn.cmd -f %s dependency:tree -DoutputFile=%s  -DoutputType=text", pomFile, depTreeFolder);
        ArrayList<String> copyOutput = runCommandWithExecutor(mvnCmd);
        for (String output : copyOutput) {
            if (output != null && output.contains("BUILD SUCCESS")) {
                Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(COPY_DEPENDENCY_TREE), StandardCharsets.UTF_8));
                InputType type = InputType.TEXT;
                Parser parser = type.newParser();
                try {
                    tree = parser.parse(r);
                    if (tree != null) {
                        LinkedList<Node> childNodes = tree.getChildNodes();
                        extracted(childNodes, dependencyOrder, dependencyLevels, 1);
                        logger.info("Drew dependency tree for  " + directoryName);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return tree;
    }

    //TODO: change method name
    private void extracted(LinkedList<Node> childNodes, List<DependencyModel> dependencyOrder, Map<Integer, List<DependencyModel>> dependencyLevels, Integer level) {
        LinkedList<Node> nodesToReIterate = new LinkedList<>();
        List<DependencyModel> dependenciesForLevel = new ArrayList<>();
        childNodes.forEach(childNode -> {
            DependencyModel dependency = new DependencyModel(childNode.getGroupId(), childNode.getArtifactId(), childNode.getVersion(), childNode.getClassifier());
            dependenciesForLevel.add(dependency);
            nodesToReIterate.addAll(childNode.getChildNodes());
        });
        dependencyOrder.addAll(dependenciesForLevel);
        dependencyLevels.put(level, dependenciesForLevel);
        if (nodesToReIterate.size() > 0) {
            extracted(nodesToReIterate, dependencyOrder, dependencyLevels, (level + 1));
        }
    }

    /**
     * Copy the client dependencies to the provided folder for the analysis
     *
     * @param directoryName       the local directory which contains the project
     * @return if the dependencies were copied to the directory
     * @throws IOException
     */
    private boolean copyProjectDependencies(String directoryName) throws IOException {
        boolean dependenciesCopied = false;
        String depCopiedFolder = System.getProperty("user.dir").replace("\\", "/") + "/"
                + COPIED_DEPENDENCIES_DIRECTORY;

        String pomFile = MVN_PROJECT_DIRECTORY + directoryName.replace("\\", "/") + "/" + POM_FILE;
        String mvnCmd = String.format("mvn.cmd -f %s dependency:copy-dependencies -DoutputDirectory=%s", pomFile, depCopiedFolder);
        ArrayList<String> copyOutput = runCommandWithExecutor(mvnCmd);
        for (String output : copyOutput) {
            if (output != null && output.contains("BUILD SUCCESS")) {
                dependenciesCopied = true;
                logger.info("Copied dependencies for  " + directoryName);
            }
        }
        return dependenciesCopied;
    }

    /**
     * Delete all files in the provided directory
     *
     * @param directory the directory in which all files to be deleted
     * @throws IOException
     */
    public void deleteFilesInDirectory(File directory) throws IOException {
        Files.walk(directory.toPath())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    /**
     * get all java classes for a given jar file
     *
     * @param givenFile jar file to extract the java classes
     * @return the java classes in the jar file
     * @throws IOException
     */
    private Set<String> getJavaClassNamesFromCompressedFiles(File givenFile) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().replace(".class", "");
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    /**
     * Extracting all java files in the entire repository
     *
     * @param dir directory of the repository (can also be the project folder of not child projects are available)
     * @return Set of java classes in the repository
     * @throws IOException
     */
    private Set<String> findJavaClassesInDirectory(String dir) throws IOException {
        Stream<Path> stream = Files.walk(Paths.get(dir));
        Set<String> javaClassesInRepo = stream
                .filter(file -> (!Files.isDirectory(file) && file.toString().endsWith(".java")))
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .map(filePath -> filePath.replace(".java", ""))
                .map(filePath -> filePath.replace("\\", "/"))
                .collect(Collectors.toSet());
        Set<String> filteredClasses = new HashSet<>();
        for (String javaClassInRepo : javaClassesInRepo) {
            if (javaClassInRepo.contains("src/main/java/")) {
                filteredClasses.add(javaClassInRepo.split("src/main/java/")[1]);
            } else if (javaClassInRepo.contains("src/test/java/")) {
                filteredClasses.add(javaClassInRepo.split("src/test/java/")[1]);
            } else {
                filteredClasses.add(javaClassInRepo);
            }
        }
        return filteredClasses;
    }

    private Set<String> findAllJavaInBuiltClasses() throws IOException {
        Set<String> javaClasses = new HashSet<>();
        // jdk source folder
        // collect all java classes in the src folder
        String jdkFolder = System.getProperty("user.dir").replace("\\", "/") + "/"
                + "../../Installations/Program Files/java/jdk1.8.0_291/src.zip";

        try (ZipFile zipFile = new ZipFile(new File(jdkFolder))) {
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = e.nextElement();
                if (zipEntry.getName().endsWith(".java")) {
                    String className = zipEntry.getName().replace(".java", "");
                    javaClasses.add(className);
                }
            }
        }


        return javaClasses;
    }


    public ArrayList<String> runCommandWithExecutor(String cmd) throws IOException {
        MvnCommandExecution mvnCommandExecution = new MvnCommandExecution();
        logger.info("Executing " + cmd);
        return mvnCommandExecution.runCommand(cmd);
    }
}
