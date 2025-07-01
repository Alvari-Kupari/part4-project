package com.example.depanalyzer.rq2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.depanalyzer.analyzer.dependencycollection.PomFile;

public class Script {

    public static void main(String[] args) {
        // Example usage
        Script script = new Script();
        System.out.println("PomFile initialized: ");
    }

    public List<Path> getValidProjects(Path repoDir) {

        try (Stream<Path> paths = Files.walk(repoDir.toPath())) {
            return paths.filter(path -> validate(path)).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Failed to navigate repo directories: " + e.getMessage());
            return null;
        }
    }

    private boolean validate(Path moduleDir) {
        try {

            if (!hasSingleRootPom(moduleDir))
                return false;
            if (!hasSingleValidSrcMainJava(moduleDir))
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
