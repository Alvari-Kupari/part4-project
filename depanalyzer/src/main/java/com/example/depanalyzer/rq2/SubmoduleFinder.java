package com.example.depanalyzer.rq2;


public class SubmoduleFinder {
  // public List<Path> getValidProjects(File repoDir) {

  //     try (Stream<Path> paths = Files.walk(repoDir.toPath())) {
  //         return paths.filter(path -> validate(path)).collect(Collectors.toList());
  //     } catch (IOException e) {
  //         System.out.println("Failed to navigate repo directories: " + e.getMessage());
  //         return null;
  //     }
  // }

  // private boolean validate(Path moduleDir) {
  //     try {

  //         if (!hasSingleRootPom(moduleDir))
  //             return false;
  //         if (!hasSingleValidSrcMainJava(moduleDir))
  //             return false;

  //     } catch (Exception e) {
  //         e.printStackTrace();
  //         return false;
  //     }

  //     return true;
  // }
}
