package nz.ac.auckland.dee.gradestyle.validator;

import java.nio.file.Path;

public class ValidatorException extends Exception {
  private Path path;

  public ValidatorException(Exception e) {
    super(e);
  }

  public ValidatorException(Path path) {
    this.path = path;
  }

  public ValidatorException(Path path, String message) {
    super(message);
    this.path = path;
  }

  public Path getPath() {
    return path;
  }
}
