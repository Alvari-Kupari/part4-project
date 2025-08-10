# Usage Report

## 1. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      JarFile jarFile = new JarFile(artifact.getFile());
                                             ^^^^^^^
```

## 2. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Artifact artifact : artifacts) {
         ^^^^^^^^
```

## 3. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Artifact artifact : artifacts) {
                  ^^^^^^^^
```

## 4. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyDatabase(Collection<Artifact> artifacts) throws IOException {
                                       ^^^^^^^^
```

## 5. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:8`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return getString(dep.getArtifact());
                         ^^^^^^^^^^^
```

## 6. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:7`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static String getString(Dependency dep) {
                                 ^^^^^^^^^^
```

## 7. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
                    ^^^^^^^^^^
```

## 8. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
                                                  ^^^^^^^^^^^^^
```

## 9. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
                                                                                   ^^^^^^^^^^
```

## 10. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private static String getString(Artifact artifact) {
                                  ^^^^^^^^
```

## 11. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:15`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areEqual(Dependency dep1, Dependency dep2) {
                                 ^^^^^^^^^^
```

## 12. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:15`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areEqual(Dependency dep1, Dependency dep2) {
                                                  ^^^^^^^^^^
```

## 13. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areSameLibrary(Dependency dep1, Dependency dep2) {
                                       ^^^^^^^^^^
```

## 14. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areSameLibrary(Dependency dep1, Dependency dep2) {
                                                        ^^^^^^^^^^
```

## 15. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
               ^^^^^^^^^^^
```

## 16. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
                             ^^^^^^^^^^
```

## 17. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
                                                      ^^^^^^^^^^^
```

## 18. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
                                                                    ^^^^^^^^^^^^^
```

## 19. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static String getLibraryName(Dependency dep) {
                                      ^^^^^^^^^^
```

## 20. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      File jarFile = artifact.getFile();
                              ^^^^^^^
```

## 21. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Artifact artifact : artifacts) {
         ^^^^^^^^
```

## 22. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Artifact artifact : artifacts) {
                  ^^^^^^^^
```

## 23. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:28`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Parser(String repoPath, Set<Artifact> artifacts, LanguageLevel javaVersion)
                                     ^^^^^^^^
```

## 24. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
    ^^^^^^^^^^^^^^^^^^^^^
```

## 25. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 26. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-spi:jar:1.6.3`

```java
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 27. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
            ^^^^^^^^^^
```

## 28. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-spi:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
                       ^^^^^^^^^^^^^^^^^^
```

## 29. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
            ^^^^^^^^^^
```

## 30. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-spi:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
                       ^^^^^^^^^^^^^^^^^^
```

## 31. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
            ^^^^^^^^^^
```

## 32. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return locator.getService(RepositorySystem.class);
                              ^^^^^^^^^^^^^^^^
```

## 33. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    return locator.getService(RepositorySystem.class);
                   ^^^^^^^^^^
```

## 34. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:18`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static RepositorySystem newRepositorySystem() {
                ^^^^^^^^^^^^^^^^
```

## 35. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 36. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
                                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 37. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        system.newLocalRepositoryManager(session, new LocalRepository("target/local-repo")));
                                                      ^^^^^^^^^^^^^^^
```

## 38. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        system.newLocalRepositoryManager(session, new LocalRepository("target/local-repo")));
                                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 39. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        system.newLocalRepositoryManager(session, new LocalRepository("target/local-repo")));
               ^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 40. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:31`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setLocalRepositoryManager(
            ^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 41. You accessed a field here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS);
                            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 42. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS);
            ^^^^^^^^^^^^^^^
```

## 43. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setIgnoreArtifactDescriptorRepositories(true); // <--- IMPORTANT
            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 44. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setCache(null);
            ^^^^^^^^
```

## 45. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static RepositorySystemSession newSession(RepositorySystem system) {
                ^^^^^^^^^^^^^^^^^^^^^^^
```

## 46. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static RepositorySystemSession newSession(RepositorySystem system) {
                                                   ^^^^^^^^^^^^^^^^
```

## 47. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:10`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
            mavenDep.getGroupId(),
                     ^^^^^^^^^^
```

## 48. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:11`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
            mavenDep.getArtifactId(),
                     ^^^^^^^^^^^^^
```

## 49. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:12`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
            mavenDep.getClassifier(),
                     ^^^^^^^^^^^^^
```

## 50. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:13`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
            mavenDep.getType(),
                     ^^^^^^^
```

## 51. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:14`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
            mavenDep.getVersion());
                     ^^^^^^^^^^
```

## 52. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:9`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new org.eclipse.aether.artifact.DefaultArtifact(
            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 53. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:9`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new org.eclipse.aether.artifact.DefaultArtifact(
        ^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 54. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:8`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    org.eclipse.aether.artifact.Artifact artifact =
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 55. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:8`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    org.eclipse.aether.artifact.Artifact artifact =
// [multi-line expression — squiggle skipped]
```

## 56. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:15`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    return new org.eclipse.aether.graph.Dependency(artifact, mavenDep.getScope());
                                                                      ^^^^^^^^
```

## 57. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:15`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return new org.eclipse.aether.graph.Dependency(artifact, mavenDep.getScope());
               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 58. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:15`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return new org.eclipse.aether.graph.Dependency(artifact, mavenDep.getScope());
           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 59. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:6`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static org.eclipse.aether.graph.Dependency toAether(
                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 60. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:7`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
      org.apache.maven.model.Dependency mavenDep) {
      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 61. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    org.eclipse.aether.artifact.Artifact artifact = aetherDep.getArtifact();
                                                              ^^^^^^^^^^^
```

## 62. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    org.eclipse.aether.artifact.Artifact artifact = aetherDep.getArtifact();
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 63. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    org.eclipse.aether.artifact.Artifact artifact = aetherDep.getArtifact();
                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 64. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    org.apache.maven.model.Dependency mavenDep = new org.apache.maven.model.Dependency();
                                                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 65. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    org.apache.maven.model.Dependency mavenDep = new org.apache.maven.model.Dependency();
                                                 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 66. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    org.apache.maven.model.Dependency mavenDep = new org.apache.maven.model.Dependency();
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 67. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    org.apache.maven.model.Dependency mavenDep = new org.apache.maven.model.Dependency();
                                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 68. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    mavenDep.setGroupId(artifact.getGroupId());
                                 ^^^^^^^^^^
```

## 69. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:22`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    mavenDep.setGroupId(artifact.getGroupId());
             ^^^^^^^^^^
```

## 70. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    mavenDep.setArtifactId(artifact.getArtifactId());
                                    ^^^^^^^^^^^^^
```

## 71. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:23`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    mavenDep.setArtifactId(artifact.getArtifactId());
             ^^^^^^^^^^^^^
```

## 72. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    mavenDep.setClassifier(artifact.getClassifier());
                                    ^^^^^^^^^^^^^
```

## 73. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:24`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    mavenDep.setClassifier(artifact.getClassifier());
             ^^^^^^^^^^^^^
```

## 74. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:25`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    mavenDep.setType(artifact.getExtension());
                              ^^^^^^^^^^^^
```

## 75. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:25`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    mavenDep.setType(artifact.getExtension());
             ^^^^^^^
```

## 76. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    mavenDep.setVersion(artifact.getVersion());
                                 ^^^^^^^^^^
```

## 77. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:26`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    mavenDep.setVersion(artifact.getVersion());
             ^^^^^^^^^^
```

## 78. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    mavenDep.setScope(aetherDep.getScope());
                                ^^^^^^^^
```

## 79. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:27`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    mavenDep.setScope(aetherDep.getScope());
             ^^^^^^^^
```

## 80. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:18`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  public static org.apache.maven.model.Dependency toMaven(
                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 81. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyAdapter.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      org.eclipse.aether.graph.Dependency aetherDep) {
      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 82. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
          ^^^^^^^^^^^^^^^^
```

## 83. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
                           ^^^^^^^^^^
```

## 84. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:13`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
          ^^^^^^^^^^^^^^^^^^^^^^^
```

## 85. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:13`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
                                  ^^^^^^^
```

## 86. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:14`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Dependency root;
          ^^^^^^^^^^
```

## 87. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:14`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Dependency root;
                     ^^^^
```

## 88. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
      ^^^^^^^^^^
```

## 89. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
                                 ^^^^^^^^^^^^^^^^
```

## 90. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
                                                              ^^^^^^^^^^^^^^^^^^^^^^^
```

## 91. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:30`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    } catch (DependencyCollectionException e) {
             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 92. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode root = request.execute(this.root);
      ^^^^^^^^^^^^^^
```

## 93. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode root = request.execute(this.root);
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 94. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public void traverse(Tree tree) throws DependencyCollectionException {
                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 95. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:15`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  private Model model;
          ^^^^^
```

## 96. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:15`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  private Model model;
                ^^^^^
```

## 97. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:19`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                                 ^^^^^^^^^^^^^^^
```

## 98. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:19`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                             ^^^^^^^^^^^^^^^^^^^^^
```

## 99. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:19`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
    ^^^^^^^^^^^^^^^
```

## 100. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:19`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 101. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    this.model = reader.read(fileReader);
                        ^^^^
```

## 102. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:17`  

**Library:** `org.codehaus.plexus:plexus-utils:jar:3.3.0`

```java
  public PomReader(Path pomfile) throws IOException, XmlPullParserException {
                                                     ^^^^^^^^^^^^^^^^^^^^^^
```

## 103. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:26`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    String source = model.getProperties().getProperty("maven.compiler.source");
                          ^^^^^^^^^^^^^
```

## 104. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:64`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    return model.getDependencies().stream()
                 ^^^^^^^^^^^^^^^
```

## 105. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomReader.java:62`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public List<Dependency> getDependencies() {
              ^^^^^^^^^^
```

## 106. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Repositories.java:9`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      new RemoteRepository.Builder("central", "default", CENTRAL_REPO_URL).build();
          ^^^^^^^^^^^^^^^^
```

## 107. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Repositories.java:8`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
                       ^^^^^^^^^^^^^^^^
```

## 108. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Repositories.java:8`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
                                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 109. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Repositories.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static final List<RemoteRepository> repositories = List.of(MAVEN_REMOTE_REPOSITORY);
                           ^^^^^^^^^^^^^^^^
```

## 110. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
          ^^^^^^^^^^^^^^^^
```

## 111. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
                           ^^^^^^^^^^
```

## 112. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
          ^^^^^^^^^^^^^^^^^^^^^^^
```

## 113. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
                                  ^^^^^^^
```

## 114. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Request(RepositorySystem repoSystem, RepositorySystemSession session) {
                 ^^^^^^^^^^^^^^^^
```

## 115. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Request(RepositorySystem repoSystem, RepositorySystemSession session) {
                                              ^^^^^^^^^^^^^^^^^^^^^^^
```

## 116. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:30`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                        ^^^^^^^^^^^^^^
```

## 117. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:30`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                    ^^^^^^^^^^^^^^^^^^^^
```

## 118. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:30`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
    ^^^^^^^^^^^^^^
```

## 119. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:30`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 120. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:31`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.setRoot(rootDependency);
                   ^^^^^^^
```

## 121. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.setRepositories(Repositories.repositories);
                   ^^^^^^^^^^^^^^^
```

## 122. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
                                             ^^^^^^^^^^^^^^^^^^^
```

## 123. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
    ^^^^^^^^^^^^^
```

## 124. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 125. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return collectResult.getRoot();
                         ^^^^^^^
```

## 126. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:29`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
         ^^^^^^^^^^^^^^
```

## 127. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:29`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
                                ^^^^^^^^^^
```

## 128. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:29`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
                                                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 129. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:39`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                        ^^^^^^^^^^^^^^
```

## 130. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:39`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                    ^^^^^^^^^^^^^^^^^^^^
```

## 131. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:39`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
    ^^^^^^^^^^^^^^
```

## 132. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:39`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 133. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.setRoot(dependency);
                   ^^^^^^^
```

## 134. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.setRepositories(Repositories.repositories);
                   ^^^^^^^^^^^^^^^
```

## 135. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
                                              ^^^^^^^^^^^^^^^^^
```

## 136. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
                                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 137. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
    ^^^^^^^^^^^^^^^^^
```

## 138. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 139. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyResult result;
    ^^^^^^^^^^^^^^^^
```

## 140. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyResult result;
                     ^^^^^^
```

## 141. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    } catch (DependencyResolutionException e) {
             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 142. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      result = repoSystem.resolveDependencies(session, dependencyRequest);
                          ^^^^^^^^^^^^^^^^^^^
```

## 143. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:53`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        .map(ArtifactResult::getArtifact)
             ^^^^^^^^^^^^^^
```

## 144. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:52`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return result.getArtifactResults().stream()
                  ^^^^^^^^^^^^^^^^^^
```

## 145. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Artifact> resolve(Dependency dependency) {
             ^^^^^^^^
```

## 146. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Artifact> resolve(Dependency dependency) {
                               ^^^^^^^^^^
```

## 147. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode root;
          ^^^^^^^^^^^^^^
```

## 148. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode root;
                         ^^^^
```

## 149. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:13`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Branch(DependencyNode root) {
                ^^^^^^^^^^^^^^
```

## 150. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode findNode(Dependency parent) {
         ^^^^^^^^^^^^^^
```

## 151. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode findNode(Dependency parent) {
                                 ^^^^^^^^^^
```

## 152. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode getRoot() {
         ^^^^^^^^^^^^^^
```

## 153. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> deps = new HashSet<>();
        ^^^^^^^^^^
```

## 154. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:25`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Collection<Dependency> getAllDeps() {
                    ^^^^^^^^^^
```

## 155. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    deps.add(node.getDependency());
                  ^^^^^^^^^^^^^
```

## 156. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                                     ^^^^^^^^^^^
```

## 157. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
         ^^^^^^^^^^^^^^
```

## 158. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                        ^^^^^
```

## 159. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void addDepsRecursive(DependencyNode node, Collection<Dependency> deps) {
                                ^^^^^^^^^^^^^^
```

## 160. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void addDepsRecursive(DependencyNode node, Collection<Dependency> deps) {
                                                                ^^^^^^^^^^
```

## 161. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    if (node.getDependency().equals(dep)) {
             ^^^^^^^^^^^^^
```

## 162. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    if (node.getDependency().equals(dep)) {
                             ^^^^^^
```

## 163. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode found = findNodeRecursive(child, dep);
      ^^^^^^^^^^^^^^
```

## 164. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode found = findNodeRecursive(child, dep);
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 165. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                                     ^^^^^^^^^^^
```

## 166. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
         ^^^^^^^^^^^^^^
```

## 167. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                        ^^^^^
```

## 168. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {
          ^^^^^^^^^^^^^^
```

## 169. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {
                                           ^^^^^^^^^^^^^^
```

## 170. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {
                                                                ^^^^^^^^^^
```

## 171. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    sb.append(node.getDependency().toString());
                   ^^^^^^^^^^^^^
```

## 172. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    sb.append(node.getDependency().toString());
                                   ^^^^^^^^
```

## 173. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    var children = node.getChildren();
                        ^^^^^^^^^^^
```

## 174. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:73`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : children) {
         ^^^^^^^^^^^^^^
```

## 175. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:73`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : children) {
                        ^^^^^
```

## 176. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:64`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void printNode(StringBuilder sb, DependencyNode node, String prefix, boolean isLast) {
                                           ^^^^^^^^^^^^^^
```

## 177. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:89`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                                     ^^^^^^^^^^^
```

## 178. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:89`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
         ^^^^^^^^^^^^^^
```

## 179. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:89`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                        ^^^^^
```

## 180. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:85`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void countRecursive(DependencyNode node, AtomicLong count) {
                              ^^^^^^^^^^^^^^
```

## 181. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public void addRoot(DependencyNode root) {
                      ^^^^^^^^^^^^^^
```

## 182. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:29`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        .map(branch -> branch.getRoot().getDependency())
                                        ^^^^^^^^^^^^^
```

## 183. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Dependency> getDirectDependencies() {
             ^^^^^^^^^^
```

## 184. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> directDeps = getDirectDependencies();
        ^^^^^^^^^^
```

## 185. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:37`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> transDeps = getClosestUniqueDependencies();
        ^^^^^^^^^^
```

## 186. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Dependency> getTransitiveDependencies() {
             ^^^^^^^^^^
```

## 187. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<DependencyNode> roots =
         ^^^^^^^^^^^^^^
```

## 188. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:54`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                new DefaultArtifact(
                    ^^^^^^^^^^^^^^^
```

## 189. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:54`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                new DefaultArtifact(
                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 190. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:53`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            new Dependency(
                ^^^^^^^^^^
```

## 191. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:53`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            new Dependency(
            ^^^^^^^^^^^^^^^^^^^
```

## 192. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:52`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new DefaultDependencyNode(
            ^^^^^^^^^^^^^^^^^^^^^
```

## 193. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:52`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new DefaultDependencyNode(
        ^^^^^^^^^^^^^^^^^^^^^^^^
```

## 194. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyNode king =
    ^^^^^^^^^^^^^^
```

## 195. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyNode king =
                   ^^^^^^^^^^^^^
```

## 196. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:58`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    king.setChildren(roots);
         ^^^^^^^^^^^
```

## 197. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Set<Dependency> getClosestUniqueDependencies() {
              ^^^^^^^^^^
```

## 198. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:64`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Queue<DependencyNode> que = new LinkedList<>();
          ^^^^^^^^^^^^^^
```

## 199. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> deps = new HashSet<>();
        ^^^^^^^^^^
```

## 200. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode next = que.poll();
      ^^^^^^^^^^^^^^
```

## 201. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode next = que.poll();
                     ^^^^^^^^^^^^^^^^^
```

## 202. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:76`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            alreadyVisited.add(DependencyUtils.getLibraryName(child.getDependency()));
                                                                    ^^^^^^^^^^^^^
```

## 203. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:79`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
          deps.add(child.getDependency());
                         ^^^^^^^^^^^^^
```

## 204. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      for (DependencyNode child : next.getChildren()) {
                                       ^^^^^^^^^^^
```

## 205. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      for (DependencyNode child : next.getChildren()) {
           ^^^^^^^^^^^^^^
```

## 206. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      for (DependencyNode child : next.getChildren()) {
                          ^^^^^
```

## 207. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:63`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Set<Dependency> bfs(Set<String> alreadyVisited, DependencyNode king) {
              ^^^^^^^^^^
```

## 208. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:63`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Set<Dependency> bfs(Set<String> alreadyVisited, DependencyNode king) {
                                                          ^^^^^^^^^^^^^^
```

## 209. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:88`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> deps = new HashSet<>();
        ^^^^^^^^^^
```

## 210. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:87`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Dependency> getAllDependencies() {
             ^^^^^^^^^^
```

## 211. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:58`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    ^^^^^^^^^^^^^^^^
```

## 212. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:58`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 213. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:59`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
    ^^^^^^^^^^^^^^^^^^^^^^^
```

## 214. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:59`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
                            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 215. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:63`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<Dependency> dependencies = pom.getDependencies();
         ^^^^^^^^^^
```

## 216. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Dependency dep : dependencies) {
         ^^^^^^^^^^
```

## 217. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Dependency dep : dependencies) {
                    ^^^
```

## 218. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:74`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Artifact> allArtifacts = new HashSet<>();
        ^^^^^^^^
```

## 219. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:75`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Artifact> transitiveArtifacts = new HashSet<>();
        ^^^^^^^^
```

## 220. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:80`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
              Set<Artifact> artifacts = new Request(system, session).resolve(dep);
                  ^^^^^^^^
```

## 221. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:87`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
              Set<Artifact> artifacts = new Request(system, session).resolve(dep);
                  ^^^^^^^^
```

## 222. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:18`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  private Model model;
          ^^^^^
```

## 223. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:18`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  private Model model;
                ^^^^^
```

## 224. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:25`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                                 ^^^^^^^^^^^^^^^
```

## 225. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:25`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                             ^^^^^^^^^^^^^^^^^^^^^
```

## 226. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:25`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
    ^^^^^^^^^^^^^^^
```

## 227. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:25`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 228. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:28`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    this.model = reader.read(fileReader);
                        ^^^^
```

## 229. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:21`  

**Library:** `org.codehaus.plexus:plexus-utils:jar:3.3.0`

```java
  public PomWriter(Path pomPath) throws IOException, XmlPullParserException {
                                                     ^^^^^^^^^^^^^^^^^^^^^^
```

## 230. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:33`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    return model.getDependencies().stream()
                 ^^^^^^^^^^^^^^^
```

## 231. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:31`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public List<Dependency> getDependencies() {
              ^^^^^^^^^^
```

## 232. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:39`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Writer writer = new MavenXpp3Writer();
                                 ^^^^^^^^^^^^^^^
```

## 233. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:39`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Writer writer = new MavenXpp3Writer();
                             ^^^^^^^^^^^^^^^^^^^^^
```

## 234. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:39`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Writer writer = new MavenXpp3Writer();
    ^^^^^^^^^^^^^^^
```

## 235. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:39`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Writer writer = new MavenXpp3Writer();
                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 236. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\dependencyupdate\PomWriter.java:41`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    writer.write(fileWriter, model);
           ^^^^^
```

## 237. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Dependency dependency;
          ^^^^^^^^^^
```

## 238. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Dependency dependency;
                     ^^^^^^^^^^
```

## 239. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private final List<RemoteRepository> repos = Repositories.repositories;
                     ^^^^^^^^^^^^^^^^
```

## 240. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
          ^^^^^^^^^^^^^^^^
```

## 241. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
                           ^^^^^^^^^^
```

## 242. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
          ^^^^^^^^^^^^^^^^^^^^^^^
```

## 243. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
                                  ^^^^^^^
```

## 244. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency dep, RepositorySystem repoSystem, RepositorySystemSession session) {
      ^^^^^^^^^^
```

## 245. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency dep, RepositorySystem repoSystem, RepositorySystemSession session) {
                      ^^^^^^^^^^^^^^^^
```

## 246. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency dep, RepositorySystem repoSystem, RepositorySystemSession session) {
                                                   ^^^^^^^^^^^^^^^^^^^^^^^
```

## 247. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-util:jar:1.6.3`

```java
    VersionScheme scheme = new GenericVersionScheme();
                               ^^^^^^^^^^^^^^^^^^^^
```

## 248. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-util:jar:1.6.3`

```java
    VersionScheme scheme = new GenericVersionScheme();
                           ^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 249. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionScheme scheme = new GenericVersionScheme();
    ^^^^^^^^^^^^^
```

## 250. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionScheme scheme = new GenericVersionScheme();
                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 251. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:37`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    String versionString = dependency.getArtifact().getVersion();
                                      ^^^^^^^^^^^
```

## 252. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:37`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    String versionString = dependency.getArtifact().getVersion();
                                                    ^^^^^^^^^^
```

## 253. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Version current = scheme.parseVersion(versionString);
                             ^^^^^^^^^^^^
```

## 254. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Version current = scheme.parseVersion(versionString);
    ^^^^^^^
```

## 255. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Version current = scheme.parseVersion(versionString);
            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 256. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getGroupId(),
                       ^^^^^^^^^^^
```

## 257. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getGroupId(),
                                     ^^^^^^^^^^
```

## 258. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:43`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getArtifactId(),
                       ^^^^^^^^^^^
```

## 259. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:43`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getArtifactId(),
                                     ^^^^^^^^^^^^^
```

## 260. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getClassifier(),
                       ^^^^^^^^^^^
```

## 261. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getClassifier(),
                                     ^^^^^^^^^^^^^
```

## 262. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getExtension(),
                       ^^^^^^^^^^^
```

## 263. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            dependency.getArtifact().getExtension(),
                                     ^^^^^^^^^^^^
```

## 264. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new DefaultArtifact(
            ^^^^^^^^^^^^^^^
```

## 265. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new DefaultArtifact(
        ^^^^^^^^^^^
```

## 266. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Artifact artifact =
    ^^^^^^^^
```

## 267. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Artifact artifact =
             ^^^^^^
```

## 268. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);
                                      ^^^^^^^^^^^^^^^^^^^
```

## 269. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);
                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 270. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);
    ^^^^^^^^^^^^^^^^^^^
```

## 271. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);
                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 272. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:49`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeResult result = repoSystem.resolveVersionRange(session, request);
                                           ^^^^^^^^^^^^^^^^^^^
```

## 273. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:49`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeResult result = repoSystem.resolveVersionRange(session, request);
    ^^^^^^^^^^^^^^^^^^
```

## 274. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:49`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    VersionRangeResult result = repoSystem.resolveVersionRange(session, request);
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 275. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<Version> versions = result.getVersions();
                                    ^^^^^^^^^^^
```

## 276. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<Version> versions = result.getVersions();
         ^^^^^^^
```

## 277. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:57`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Version v = versions.get(i);
      ^^^^^^^
```

## 278. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:57`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Version v = versions.get(i);
              ^^^^^^^^^^^^^^^^^^^
```

## 279. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:65`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        Version next = versions.get(i + 1);
        ^^^^^^^
```

## 280. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:65`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        Version next = versions.get(i + 1);
                ^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 281. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:66`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        dependency.getArtifact().setVersion(next.toString());
                                                 ^^^^^^^^
```

## 282. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:66`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        dependency.getArtifact().setVersion(next.toString());
                   ^^^^^^^^^^^
```

## 283. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:66`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        dependency.getArtifact().setVersion(next.toString());
                                 ^^^^^^^^^^
```

## 284. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      throws VersionRangeResolutionException, InvalidVersionSpecificationException {
             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 285. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\DependencyUpdate.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      throws VersionRangeResolutionException, InvalidVersionSpecificationException {
                                              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 286. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<Dependency> deps = pomWriter.getDependencies();
         ^^^^^^^^^^
```

## 287. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    ^^^^^^^^^^^^^^^^
```

## 288. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 289. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
    ^^^^^^^^^^^^^^^^^^^^^^^
```

## 290. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
                            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 291. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Dependency dep : deps) {
         ^^^^^^^^^^
```

## 292. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\rq2\Script.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Dependency dep : deps) {
                    ^^^
```


**Total usages:** 292

