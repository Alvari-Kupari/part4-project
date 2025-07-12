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

## 5. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:80`  

**Library:** `org.apache.maven:maven-artifact:jar:3.8.5`

```java
    org.apache.maven.artifact.Artifact art;
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 6. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:80`  

**Library:** `org.apache.maven:maven-artifact:jar:3.8.5`

```java
    org.apache.maven.artifact.Artifact art;
                                       ^^^
```

## 7. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:81`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Artifact a;
    ^^^^^^^^
```

## 8. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:81`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Artifact a;
             ^
```

## 9. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyDatabase.java:101`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private String getArtifactString(Artifact artifact) {
                                   ^^^^^^^^
```

## 10. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:8`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return getString(dep.getArtifact());
                         ^^^^^^^^^^^
```

## 11. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:7`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static String getString(Dependency dep) {
                                 ^^^^^^^^^^
```

## 12. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
                    ^^^^^^^^^^
```

## 13. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
                                                  ^^^^^^^^^^^^^
```

## 14. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
                                                                                   ^^^^^^^^^^
```

## 15. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private static String getString(Artifact artifact) {
                                  ^^^^^^^^
```

## 16. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:15`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areEqual(Dependency dep1, Dependency dep2) {
                                 ^^^^^^^^^^
```

## 17. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:15`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areEqual(Dependency dep1, Dependency dep2) {
                                                  ^^^^^^^^^^
```

## 18. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areSameLibrary(Dependency dep1, Dependency dep2) {
                                       ^^^^^^^^^^
```

## 19. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static boolean areSameLibrary(Dependency dep1, Dependency dep2) {
                                                        ^^^^^^^^^^
```

## 20. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
               ^^^^^^^^^^^
```

## 21. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
                             ^^^^^^^^^^
```

## 22. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
                                                      ^^^^^^^^^^^
```

## 23. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
                                                                    ^^^^^^^^^^^^^
```

## 24. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\DependencyUtils.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static String getLibraryName(Dependency dep) {
                                      ^^^^^^^^^^
```

## 25. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      File jarFile = artifact.getFile();
                              ^^^^^^^
```

## 26. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Artifact artifact : artifacts) {
         ^^^^^^^^
```

## 27. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:41`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Artifact artifact : artifacts) {
                  ^^^^^^^^
```

## 28. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\Parser.java:28`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Parser(String repoPath, Set<Artifact> artifacts, LanguageLevel javaVersion)
                                     ^^^^^^^^
```

## 29. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
    ^^^^^^^^^^^^^^^^^^^^^
```

## 30. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:19`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 31. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-spi:jar:1.6.3`

```java
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 32. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:20`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
            ^^^^^^^^^^
```

## 33. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-spi:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
                       ^^^^^^^^^^^^^^^^^^
```

## 34. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
            ^^^^^^^^^^
```

## 35. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-spi:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
                       ^^^^^^^^^^^^^^^^^^
```

## 36. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
            ^^^^^^^^^^
```

## 37. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return locator.getService(RepositorySystem.class);
                              ^^^^^^^^^^^^^^^^
```

## 38. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-impl:jar:1.6.3`

```java
    return locator.getService(RepositorySystem.class);
                   ^^^^^^^^^^
```

## 39. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:18`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static RepositorySystem newRepositorySystem() {
                ^^^^^^^^^^^^^^^^
```

## 40. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 41. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
                                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 42. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        system.newLocalRepositoryManager(session, new LocalRepository("target/local-repo")));
                                                      ^^^^^^^^^^^^^^^
```

## 43. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        system.newLocalRepositoryManager(session, new LocalRepository("target/local-repo")));
                                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 44. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        system.newLocalRepositoryManager(session, new LocalRepository("target/local-repo")));
               ^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 45. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:31`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setLocalRepositoryManager(
            ^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 46. You accessed a field here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS);
                            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 47. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS);
            ^^^^^^^^^^^^^^^
```

## 48. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setIgnoreArtifactDescriptorRepositories(true); // <--- IMPORTANT
            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 49. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    session.setCache(null);
            ^^^^^^^^
```

## 50. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static RepositorySystemSession newSession(RepositorySystem system) {
                ^^^^^^^^^^^^^^^^^^^^^^^
```

## 51. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\analysis\RepositorySystemFactory.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public static RepositorySystemSession newSession(RepositorySystem system) {
                                                   ^^^^^^^^^^^^^^^^
```

## 52. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
          ^^^^^^^^^^^^^^^^
```

## 53. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:12`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
                           ^^^^^^^^^^
```

## 54. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:13`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
          ^^^^^^^^^^^^^^^^^^^^^^^
```

## 55. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:13`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
                                  ^^^^^^^
```

## 56. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:14`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Dependency root;
          ^^^^^^^^^^
```

## 57. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:14`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Dependency root;
                     ^^^^
```

## 58. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
      ^^^^^^^^^^
```

## 59. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
                                 ^^^^^^^^^^^^^^^^
```

## 60. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
                                                              ^^^^^^^^^^^^^^^^^^^^^^^
```

## 61. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:30`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    } catch (DependencyCollectionException e) {
             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 62. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode root = request.execute(this.root);
      ^^^^^^^^^^^^^^
```

## 63. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode root = request.execute(this.root);
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 64. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\DependencyTraverser.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public void traverse(Tree tree) throws DependencyCollectionException {
                                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 65. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:16`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  private Model model;
          ^^^^^
```

## 66. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:16`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
  private Model model;
                ^^^^^
```

## 67. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                                 ^^^^^^^^^^^^^^^
```

## 68. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                             ^^^^^^^^^^^^^^^^^^^^^
```

## 69. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
    ^^^^^^^^^^^^^^^
```

## 70. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:21`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    MavenXpp3Reader reader = new MavenXpp3Reader();
                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 71. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:23`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    this.model = reader.read(fileReader);
                        ^^^^
```

## 72. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:18`  

**Library:** `org.codehaus.plexus:plexus-utils:jar:3.3.0`

```java
  public PomFile(String pomFile) throws IOException, XmlPullParserException {
                                                     ^^^^^^^^^^^^^^^^^^^^^^
```

## 73. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:28`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    String source = model.getProperties().getProperty("maven.compiler.source");
                          ^^^^^^^^^^^^^
```

## 74. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:71`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
                        dep.getGroupId(),
                            ^^^^^^^^^^
```

## 75. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:72`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
                        dep.getArtifactId(),
                            ^^^^^^^^^^^^^
```

## 76. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:73`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
                        dep.getClassifier(),
                            ^^^^^^^^^^^^^
```

## 77. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:74`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
                        dep.getType(),
                            ^^^^^^^
```

## 78. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:75`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
                        dep.getVersion()),
                            ^^^^^^^^^^
```

## 79. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                    new DefaultArtifact(
                        ^^^^^^^^^^^^^^^
```

## 80. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                    new DefaultArtifact(
                    ^^^^^^^^^^^^^^^^^^^^^
```

## 81. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:76`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
                    dep.getScope()))
                        ^^^^^^^^
```

## 82. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:69`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                new Dependency(
                    ^^^^^^^^^^
```

## 83. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:69`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                new Dependency(
                ^^^^^^^^^^^^^^^^^^^
```

## 84. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:66`  

**Library:** `org.apache.maven:maven-model:jar:3.8.5`

```java
    return model.getDependencies().stream()
                 ^^^^^^^^^^^^^^^
```

## 85. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:64`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public List<Dependency> getDependencies() throws IOException, XmlPullParserException {
              ^^^^^^^^^^
```

## 86. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\PomFile.java:64`  

**Library:** `org.codehaus.plexus:plexus-utils:jar:3.3.0`

```java
  public List<Dependency> getDependencies() throws IOException, XmlPullParserException {
                                                                ^^^^^^^^^^^^^^^^^^^^^^
```

## 87. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:22`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      new RemoteRepository.Builder("central", "default", CENTRAL_REPO_URL).build();
          ^^^^^^^^^^^^^^^^
```

## 88. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
                       ^^^^^^^^^^^^^^^^
```

## 89. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
                                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 90. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
          ^^^^^^^^^^^^^^^^
```

## 91. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:24`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystem repoSystem;
                           ^^^^^^^^^^
```

## 92. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:25`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
          ^^^^^^^^^^^^^^^^^^^^^^^
```

## 93. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:25`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private RepositorySystemSession session;
                                  ^^^^^^^
```

## 94. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Request(RepositorySystem repoSystem, RepositorySystemSession session) {
                 ^^^^^^^^^^^^^^^^
```

## 95. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Request(RepositorySystem repoSystem, RepositorySystemSession session) {
                                              ^^^^^^^^^^^^^^^^^^^^^^^
```

## 96. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                        ^^^^^^^^^^^^^^
```

## 97. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                    ^^^^^^^^^^^^^^^^^^^^
```

## 98. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
    ^^^^^^^^^^^^^^
```

## 99. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:34`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 100. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.setRoot(rootDependency);
                   ^^^^^^^
```

## 101. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:36`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.addRepository(MAVEN_REMOTE_REPOSITORY);
                   ^^^^^^^^^^^^^
```

## 102. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
                                             ^^^^^^^^^^^^^^^^^^^
```

## 103. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
    ^^^^^^^^^^^^^
```

## 104. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:38`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 105. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:39`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return collectResult.getRoot();
                         ^^^^^^^
```

## 106. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
         ^^^^^^^^^^^^^^
```

## 107. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
                                ^^^^^^^^^^
```

## 108. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
                                                                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 109. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:43`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                        ^^^^^^^^^^^^^^
```

## 110. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:43`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                                    ^^^^^^^^^^^^^^^^^^^^
```

## 111. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:43`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
    ^^^^^^^^^^^^^^
```

## 112. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:43`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    CollectRequest collectRequest = new CollectRequest();
                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 113. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:44`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.setRoot(dependency);
                   ^^^^^^^
```

## 114. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    collectRequest.addRepository(MAVEN_REMOTE_REPOSITORY);
                   ^^^^^^^^^^^^^
```

## 115. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
                                              ^^^^^^^^^^^^^^^^^
```

## 116. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
                                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 117. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
    ^^^^^^^^^^^^^^^^^
```

## 118. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 119. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyResult result;
    ^^^^^^^^^^^^^^^^
```

## 120. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyResult result;
                     ^^^^^^
```

## 121. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    } catch (DependencyResolutionException e) {
             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 122. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:50`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      result = repoSystem.resolveDependencies(session, dependencyRequest);
                          ^^^^^^^^^^^^^^^^^^^
```

## 123. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:57`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        .map(ArtifactResult::getArtifact)
             ^^^^^^^^^^^^^^
```

## 124. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:56`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    return result.getArtifactResults().stream()
                  ^^^^^^^^^^^^^^^^^^
```

## 125. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Artifact> resolve(Dependency dependency) {
             ^^^^^^^^
```

## 126. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencycollection\Request.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Artifact> resolve(Dependency dependency) {
                               ^^^^^^^^^^
```

## 127. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode root;
          ^^^^^^^^^^^^^^
```

## 128. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:11`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode root;
                         ^^^^
```

## 129. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:13`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Branch(DependencyNode root) {
                ^^^^^^^^^^^^^^
```

## 130. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode findNode(Dependency parent) {
         ^^^^^^^^^^^^^^
```

## 131. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:17`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode findNode(Dependency parent) {
                                 ^^^^^^^^^^
```

## 132. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:21`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public DependencyNode getRoot() {
         ^^^^^^^^^^^^^^
```

## 133. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:26`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> deps = new HashSet<>();
        ^^^^^^^^^^
```

## 134. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:25`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Collection<Dependency> getAllDeps() {
                    ^^^^^^^^^^
```

## 135. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    deps.add(node.getDependency());
                  ^^^^^^^^^^^^^
```

## 136. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                                     ^^^^^^^^^^^
```

## 137. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
         ^^^^^^^^^^^^^^
```

## 138. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                        ^^^^^
```

## 139. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void addDepsRecursive(DependencyNode node, Collection<Dependency> deps) {
                                ^^^^^^^^^^^^^^
```

## 140. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:32`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void addDepsRecursive(DependencyNode node, Collection<Dependency> deps) {
                                                                ^^^^^^^^^^
```

## 141. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    if (node.getDependency().equals(dep)) {
             ^^^^^^^^^^^^^
```

## 142. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:42`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    if (node.getDependency().equals(dep)) {
                             ^^^^^^
```

## 143. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode found = findNodeRecursive(child, dep);
      ^^^^^^^^^^^^^^
```

## 144. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:47`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode found = findNodeRecursive(child, dep);
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 145. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                                     ^^^^^^^^^^^
```

## 146. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
         ^^^^^^^^^^^^^^
```

## 147. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:46`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                        ^^^^^
```

## 148. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {
          ^^^^^^^^^^^^^^
```

## 149. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {
                                           ^^^^^^^^^^^^^^
```

## 150. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:40`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {
                                                                ^^^^^^^^^^
```

## 151. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    sb.append(node.getDependency().toString());
                   ^^^^^^^^^^^^^
```

## 152. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    sb.append(node.getDependency().toString());
                                   ^^^^^^^^
```

## 153. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    var children = node.getChildren();
                        ^^^^^^^^^^^
```

## 154. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:73`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : children) {
         ^^^^^^^^^^^^^^
```

## 155. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:73`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : children) {
                        ^^^^^
```

## 156. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:64`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void printNode(StringBuilder sb, DependencyNode node, String prefix, boolean isLast) {
                                           ^^^^^^^^^^^^^^
```

## 157. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:89`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                                     ^^^^^^^^^^^
```

## 158. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:89`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
         ^^^^^^^^^^^^^^
```

## 159. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:89`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (DependencyNode child : node.getChildren()) {
                        ^^^^^
```

## 160. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Branch.java:85`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private void countRecursive(DependencyNode node, AtomicLong count) {
                              ^^^^^^^^^^^^^^
```

## 161. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:23`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public void addRoot(DependencyNode root) {
                      ^^^^^^^^^^^^^^
```

## 162. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:29`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        .map(branch -> branch.getRoot().getDependency())
                                        ^^^^^^^^^^^^^
```

## 163. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:27`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Dependency> getDirectDependencies() {
             ^^^^^^^^^^
```

## 164. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:35`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> directDeps = getDirectDependencies();
        ^^^^^^^^^^
```

## 165. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:37`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> transDeps = getClosestUniqueDependencies();
        ^^^^^^^^^^
```

## 166. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:33`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Dependency> getTransitiveDependencies() {
             ^^^^^^^^^^
```

## 167. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:48`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<DependencyNode> roots =
         ^^^^^^^^^^^^^^
```

## 168. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:54`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                new DefaultArtifact(
                    ^^^^^^^^^^^^^^^
```

## 169. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:54`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
                new DefaultArtifact(
                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 170. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:53`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            new Dependency(
                ^^^^^^^^^^
```

## 171. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:53`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            new Dependency(
            ^^^^^^^^^^^^^^^^^^^
```

## 172. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:52`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new DefaultDependencyNode(
            ^^^^^^^^^^^^^^^^^^^^^
```

## 173. You instantiated an object here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:52`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
        new DefaultDependencyNode(
        ^^^^^^^^^^^^^^^^^^^^^^^^
```

## 174. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyNode king =
    ^^^^^^^^^^^^^^
```

## 175. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    DependencyNode king =
                   ^^^^^^^^^^^^^
```

## 176. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:58`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    king.setChildren(roots);
         ^^^^^^^^^^^
```

## 177. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:45`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Set<Dependency> getClosestUniqueDependencies() {
              ^^^^^^^^^^
```

## 178. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:64`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Queue<DependencyNode> que = new LinkedList<>();
          ^^^^^^^^^^^^^^
```

## 179. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> deps = new HashSet<>();
        ^^^^^^^^^^
```

## 180. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode next = que.poll();
      ^^^^^^^^^^^^^^
```

## 181. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:70`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      DependencyNode next = que.poll();
                     ^^^^^^^^^^^^^^^^^
```

## 182. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:76`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
            alreadyVisited.add(DependencyUtils.getLibraryName(child.getDependency()));
                                                                    ^^^^^^^^^^^^^
```

## 183. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:79`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
          deps.add(child.getDependency());
                         ^^^^^^^^^^^^^
```

## 184. You called a method here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      for (DependencyNode child : next.getChildren()) {
                                       ^^^^^^^^^^^
```

## 185. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      for (DependencyNode child : next.getChildren()) {
           ^^^^^^^^^^^^^^
```

## 186. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
      for (DependencyNode child : next.getChildren()) {
                          ^^^^^
```

## 187. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:63`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Set<Dependency> bfs(Set<String> alreadyVisited, DependencyNode king) {
              ^^^^^^^^^^
```

## 188. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:63`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  private Set<Dependency> bfs(Set<String> alreadyVisited, DependencyNode king) {
                                                          ^^^^^^^^^^^^^^
```

## 189. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:88`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Dependency> deps = new HashSet<>();
        ^^^^^^^^^^
```

## 190. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\analyzer\dependencytree\Tree.java:87`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
  public Set<Dependency> getAllDependencies() {
             ^^^^^^^^^^
```

## 191. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:50`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    ^^^^^^^^^^^^^^^^
```

## 192. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:50`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 193. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
    ^^^^^^^^^^^^^^^^^^^^^^^
```

## 194. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:51`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
                            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
```

## 195. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:55`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    List<Dependency> dependencies = pom.getDependencies();
         ^^^^^^^^^^
```

## 196. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:59`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Dependency dep : dependencies) {
         ^^^^^^^^^^
```

## 197. You declared a variable here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:59`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    for (Dependency dep : dependencies) {
                    ^^^
```

## 198. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:66`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Artifact> allArtifacts = new HashSet<>();
        ^^^^^^^^
```

## 199. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:67`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
    Set<Artifact> transitiveArtifacts = new HashSet<>();
        ^^^^^^^^
```

## 200. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:72`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
              Set<Artifact> artifacts = new Request(system, session).resolve(dep);
                  ^^^^^^^^
```

## 201. You referenced a class/interface here

**Location:** `C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\depanalyzer\src\main\java\com\example\depanalyzer\Main.java:79`  

**Library:** `org.apache.maven.resolver:maven-resolver-api:jar:1.6.3`

```java
              Set<Artifact> artifacts = new Request(system, session).resolve(dep);
                  ^^^^^^^^
```


**Total usages:** 201

