package com.example.depanalyzer.rq2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.depanalyzer.analyzer.dependencycollection.PomFile;

import japicmp.cmp.JApiCmpArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiClass;

public class Script {

    public static void main(String[] args) {
        // Example usage
        Script script = new Script();
        System.out.println("PomFile initialized: ");

        JarArchiveComparatorOptions comparatorOptions = new JarArchiveComparatorOptions();
        JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(comparatorOptions);
        JApiCmpArchive archive = new JApiCmpArchive(jarFilePath, null);
        List<JApiClass> jApiClasses = jarArchiveComparator.compare(oldArchives, newArchives);
    }

}
