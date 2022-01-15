package analisador_semantico.tables;

import java.util.*;

public class ProgramTable {
  String id;
  HashMap<Symbol, ClassTable> classes;

  public ProgramTable(String id) {
    this.id = id;
    this.classes = new HashMap<Symbol, ClassTable>();
  }

  public boolean putClass(Symbol symbol, ClassTable cl) {
    if (this.classes.get(symbol) == null) {
      this.classes.put(symbol, cl);
      return true;
    }

    return false;
  }

  public ClassTable getClass(Symbol symbol) {
    return this.classes.get(symbol);
  }

  public HashMap<Symbol, ClassTable> getClasses() {
    return this.classes;
  }

  public String toString() {
    return this.id;
  }
}
