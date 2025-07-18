package com.example.depanalyzer.analyzer.dependencycollection;

import java.util.List;
import org.eclipse.aether.repository.RemoteRepository;

public class Repositories {
  private static final String CENTRAL_REPO_URL = "https://repo.maven.apache.org/maven2";
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
      new RemoteRepository.Builder("central", "default", CENTRAL_REPO_URL).build();

  public static final List<RemoteRepository> repositories = List.of(MAVEN_REMOTE_REPOSITORY);
}
