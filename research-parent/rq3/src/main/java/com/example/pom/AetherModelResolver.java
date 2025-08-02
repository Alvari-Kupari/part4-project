package com.example.pom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;

public class AetherModelResolver implements ModelResolver {

  private final RepositorySystem repoSystem;
  private final RepositorySystemSession session;
  private final List<RemoteRepository> repositories;

  public AetherModelResolver(
      RepositorySystem repoSystem,
      RepositorySystemSession session,
      List<RemoteRepository> repositories) {
    this.repoSystem = repoSystem;
    this.session = session;
    this.repositories = new ArrayList<>(repositories);
  }

  @Override
  public ModelSource resolveModel(String groupId, String artifactId, String version)
      throws UnresolvableModelException {
    return resolve(groupId, artifactId, version);
  }

  @Override
  public ModelSource resolveModel(Parent parent) throws UnresolvableModelException {
    // Try relative path first
    String relPath = parent.getRelativePath();
    if (relPath == null || relPath.isEmpty()) {
      relPath = "../pom.xml"; // Maven's default
    }

    File localPom = new File(relPath);
    if (localPom.exists()) {
      return new ModelSource2() {
        @Override
        public InputStream getInputStream() throws IOException {
          return new FileInputStream(localPom);
        }

        @Override
        public String getLocation() {
          return localPom.getAbsolutePath();
        }

        @Override
        public ModelSource2 getRelatedSource(String relPath) {
          File relatedFile = new File(localPom.getParentFile(), relPath);
          return new ModelSource2() {
            @Override
            public InputStream getInputStream() throws IOException {
              return new FileInputStream(relatedFile);
            }

            @Override
            public String getLocation() {
              return relatedFile.getAbsolutePath();
            }

            @Override
            public ModelSource2 getRelatedSource(String relPath) {
              return null;
            }

            @Override
            public URI getLocationURI() {
              return relatedFile.toURI();
            }
          };
        }

        @Override
        public URI getLocationURI() {
          return localPom.toURI();
        }
      };
    }

    // Fallback to repository resolution
    return resolve(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
  }

  @Override
  public ModelSource resolveModel(Dependency dependency) throws UnresolvableModelException {
    return resolve(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
  }

  private ModelSource resolve(String groupId, String artifactId, String version)
      throws UnresolvableModelException {
    try {
      Artifact artifact = new DefaultArtifact(groupId, artifactId, "pom", version);
      ArtifactRequest request = new ArtifactRequest(artifact, repositories, null);
      ArtifactResult result = repoSystem.resolveArtifact(session, request);
      File file = result.getArtifact().getFile();

      return new ModelSource2() {

        @Override
        public InputStream getInputStream() throws IOException {
          return new FileInputStream(file);
        }

        @Override
        public String getLocation() {
          return file.getAbsolutePath();
        }

        @Override
        public ModelSource2 getRelatedSource(String relPath) {
          File relatedFile = new File(file.getParentFile(), relPath);
          return new ModelSource2() {
            @Override
            public InputStream getInputStream() throws IOException {
              return new FileInputStream(relatedFile);
            }

            @Override
            public String getLocation() {
              return relatedFile.getAbsolutePath();
            }

            @Override
            public ModelSource2 getRelatedSource(String relPath) {
              return null; // or throw if you prefer
            }

            @Override
            public URI getLocationURI() {
              return relatedFile.toURI();
            }
          };
        }

        @Override
        public URI getLocationURI() {
          return file.toURI();
        }
      };

    } catch (Exception e) {
      throw new UnresolvableModelException(
          "Could not resolve model: " + groupId + ":" + artifactId + ":" + version,
          groupId,
          artifactId,
          version,
          e);
    }
  }

  @Override
  public void addRepository(Repository repository) throws InvalidRepositoryException {
    addRepository(repository, false);
  }

  @Override
  public void addRepository(Repository repository, boolean replace)
      throws InvalidRepositoryException {
    RemoteRepository remoteRepo =
        new RemoteRepository.Builder(repository.getId(), "default", repository.getUrl()).build();

    if (replace) {
      repositories.removeIf(r -> r.getId().equals(repository.getId()));
    }
    repositories.add(remoteRepo);
  }

  @Override
  public ModelResolver newCopy() {
    return new AetherModelResolver(repoSystem, session, repositories);
  }
}
