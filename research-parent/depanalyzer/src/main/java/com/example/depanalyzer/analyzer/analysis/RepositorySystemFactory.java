package com.example.depanalyzer.analyzer.analysis;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

public class RepositorySystemFactory {

  // Singleton instances to avoid recreating expensive objects
  private static volatile RepositorySystem repositorySystem;
  private static volatile RepositorySystemSession repositorySystemSession;
  
  // Shared local repository path to avoid duplicate downloads
  private static final String SHARED_LOCAL_REPO = "target/shared-local-repo";

  /**
   * Gets a shared RepositorySystem instance (thread-safe singleton)
   */
  public static RepositorySystem getSharedRepositorySystem() {
    if (repositorySystem == null) {
      synchronized (RepositorySystemFactory.class) {
        if (repositorySystem == null) {
          repositorySystem = createRepositorySystem();
        }
      }
    }
    return repositorySystem;
  }

  /**
   * Gets a shared RepositorySystemSession instance (thread-safe singleton)
   */
  public static RepositorySystemSession getSharedSession() {
    if (repositorySystemSession == null) {
      synchronized (RepositorySystemFactory.class) {
        if (repositorySystemSession == null) {
          repositorySystemSession = createSession(getSharedRepositorySystem());
        }
      }
    }
    return repositorySystemSession;
  }

  /**
   * @deprecated Use getSharedRepositorySystem() for better performance
   */
  @Deprecated
  public static RepositorySystem newRepositorySystem() {
    return createRepositorySystem();
  }

  /**
   * @deprecated Use getSharedSession() for better performance
   */
  @Deprecated
  public static RepositorySystemSession newSession(RepositorySystem system) {
    return createSession(system);
  }

  private static RepositorySystem createRepositorySystem() {
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
    locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
    return locator.getService(RepositorySystem.class);
  }

  private static RepositorySystemSession createSession(RepositorySystem system) {
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
    
    // Use shared local repository to avoid duplicate downloads
    session.setLocalRepositoryManager(
        system.newLocalRepositoryManager(session, new LocalRepository(SHARED_LOCAL_REPO)));
    
    session.setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS);
    session.setIgnoreArtifactDescriptorRepositories(true);
    
    // PERFORMANCE OPTIMIZATION: Enable caching instead of disabling it
    // session.setCache(null); // REMOVED - this was disabling important caching
    
    return session;
  }
}
