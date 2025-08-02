package com.example.pom;

import org.apache.maven.model.building.ModelBuildingException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class PomException extends Exception {
  public PomException(XmlPullParserException e) {
    super("XML PULL PARSER EXCEPTION ", e);
  }

  public PomException(ModelBuildingException e) {
    super("Error building the model", e);
  }
}
