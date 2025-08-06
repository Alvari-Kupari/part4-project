package com.example.stage2;

import com.example.SubModule;
import com.example.stage1.breakingchange.BreakingChange;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;

public class CsvReader {

  public static class CsvRow {
    public final Dependency currentDependency;
    public final Dependency latestMinorDependency;
    public final BreakingChange breakingChange;

    public CsvRow(
        Dependency currentDependency,
        Dependency latestMinorDependency,
        BreakingChange breakingChange) {
      this.currentDependency = currentDependency;
      this.latestMinorDependency = latestMinorDependency;
      this.breakingChange = breakingChange;
    }
  }

  private final Path path;

  public CsvReader(Path path) {
    this.path = path;
  }

  public List<CsvRow> read() throws IOException {
    List<CsvRow> rows = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      String header = reader.readLine(); // skip header
      String line;
      while ((line = reader.readLine()) != null) {
        List<String> values = parseCsvLine(line);
        if (values.size() != 9) continue;

        String[] coords = values.get(0).split(":");
        String groupId = coords[0];
        String artifactId = coords[1];
        String currentVersion = values.get(1);
        String latestMinorVersion = values.get(2);

        Dependency currentDep =
            new Dependency(
                new DefaultArtifact(groupId, artifactId, "jar", currentVersion), "compile");
        Dependency latestMinorDep =
            new Dependency(
                new DefaultArtifact(groupId, artifactId, "jar", latestMinorVersion), "compile");

        BreakingChange change =
            new BreakingChange.Builder()
                .className(values.get(3))
                .memberName(values.get(4))
                .changeType(values.get(5))
                .description(values.get(6))
                .libraryName(artifactId)
                .oldVersion(currentVersion)
                .newVersion(latestMinorVersion)
                .isBinaryCompatible(Boolean.parseBoolean(values.get(7)))
                .isSourceCompatible(Boolean.parseBoolean(values.get(8)))
                .build();

        rows.add(new CsvRow(currentDep, latestMinorDep, change));
      }
    }

    return rows;
  }

  private List<String> parseCsvLine(String line) {
    List<String> result = new ArrayList<>();
    boolean inQuotes = false;
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (inQuotes) {
        if (c == '\"') {
          if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
            sb.append('\"');
            i++; // skip next
          } else {
            inQuotes = false;
          }
        } else {
          sb.append(c);
        }
      } else {
        if (c == '\"') {
          inQuotes = true;
        } else if (c == ',') {
          result.add(sb.toString());
          sb.setLength(0);
        } else {
          sb.append(c);
        }
      }
    }

    result.add(sb.toString()); // last field
    return result;
  }

  public static CsvReader getCsv(SubModule submodule, Path csvfolder) {
    String csvName = getCsvName(submodule);
    Path csvPath = csvfolder.resolve(csvName);

    return new CsvReader(csvPath);
  }

  private static String getCsvName(SubModule submodule) {
    return submodule.getRepo().getName() + "_" + submodule.getName() + ".csv";
  }
}
