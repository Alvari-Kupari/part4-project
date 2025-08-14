package com.example.csv;

import com.example.SubModule;
import com.example.normalisation.Symbol;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * CSV writer specifically for outputting all symbol uses, along with their metadata in the client
 * code.
 */
public class ClientSymbolCsvWriter {
  private final Path path;
  private final FileWriter writer;
  private boolean headerWritten = false;

  public ClientSymbolCsvWriter(Path path) throws IOException {
    this.path = path;
    boolean fileExists = Files.exists(path);
    this.writer = new FileWriter(path.toFile(), true); // Append mode
    this.headerWritten = fileExists;
    if (!fileExists) {
      writeHeader();
    }
  }

  public ClientSymbolCsvWriter(SubModule submodule, Path csvFolder, String suffix)
      throws IOException {
    String csvFileName =
        submodule.getRepo().getName() + "_" + submodule.getName() + suffix + ".csv";
    this.path = csvFolder.resolve(csvFileName);
    boolean fileExists = Files.exists(this.path);
    this.writer = new FileWriter(this.path.toFile(), true); // Append mode
    this.headerWritten = fileExists;
    if (!fileExists) {
      writeHeader();
    }
  }

  private void writeHeader() throws IOException {
    writer.append("Library_Name,Symbol_Type,Class_Name,Symbol_Name,Usage_Location,Line_Number\n");
    writer.flush();
  }

  public void writeAllSymbols(Collection<Symbol> symbols) throws IOException {
    for (Symbol symbol : symbols) {
      writeSymbol(symbol);
    }
    writer.flush();
  }

  private void writeSymbol(Symbol symbol) throws IOException {
    writer.append(
        String.format(
            "%s,%s,%s,%s,%s,%d\n",
            escapeCSV(symbol.getLibrary() == null ? null : symbol.getLibrary().toString()),
            escapeCSV(symbol.getSymbolType()),
            escapeCSV(symbol.getClassName()),
            escapeCSV(symbol.getSymbolName()),
            escapeCSV(symbol.getUsageLocation()),
            symbol.getLineNumber()));
  }

  private String escapeCSV(String value) {
    if (value == null) return "";
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }

  public void close() throws IOException {
    writer.close();
  }
}
