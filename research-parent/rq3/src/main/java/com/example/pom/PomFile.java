package com.example.pom;

import com.example.depanalyzer.analyzer.dependencycollection.DependencyAdapter;
import com.example.depanalyzer.analyzer.dependencycollection.Repositories;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

public class PomFile {
  private final Model model;

  public PomFile(Path pomPath, RepositorySystem system, RepositorySystemSession session)
      throws IOException, PomException {
    File pomFile = pomPath.toFile();
    if (!pomFile.exists()) {
      throw new IOException("No pomfile found at: " + pomPath);
    }
    try {
      this.model = buildEffectiveModel(pomFile, system, session);
    } catch (ModelBuildingException e) {
      throw new PomException(e);
    }
  }

  private Model buildEffectiveModel(
      File pomFile, RepositorySystem system, RepositorySystemSession session)
      throws ModelBuildingException {
    ModelBuildingRequest request = new DefaultModelBuildingRequest();
    request.setProcessPlugins(false);
    request.setPomFile(pomFile);
    request.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
    request.setSystemProperties(System.getProperties());
    request.setModelResolver(new AetherModelResolver(system, session, Repositories.repositories));

    return new DefaultModelBuilderFactory().newInstance().build(request).getEffectiveModel();
  }

  public List<Dependency> getDependencies() {
    List<org.apache.maven.model.Dependency> deps = model.getDependencies();
    return deps.stream().map(d -> DependencyAdapter.toAether(d)).collect(Collectors.toList());
  }
}
