package com.example.normalisation;

import java.util.HashSet;
import java.util.Set;

public class SymbolDatabase {

  private Set<Symbol> symbols;

  public SymbolDatabase() {
    this.symbols = new HashSet<>();
  }

  public void add(Symbol symbol) {
    symbols.add(symbol);
  }

  public Set<Symbol> getSymbols() {
    return symbols;
  }

  public int getSize() {
    return symbols.size();
  }
}
