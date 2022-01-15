package analisador_semantico.tables;

import analisador_semantico.syntaxtree.*;
import java.util.*;
//import tables.*;

public class ClassTable {
  public String id, extended_class_id;
  public HashMap<Symbol, Type> fields;
  public HashMap<Symbol, MethodTable> methods;

  public ClassTable(String id, String extended_class_id) {
    this.id = id;
    this.extended_class_id = extended_class_id;
    fields = new HashMap<Symbol, Type>();
    methods = new HashMap<Symbol, MethodTable>();
  }

  public boolean putField(Symbol symbol, Type type) {
    if (this.fields.get(symbol) == null) {
      this.fields.put(symbol, type);
      return true;
    }
    
    return false;
  }

  public Type getField(Symbol symbol) {
    return this.fields.get(symbol);
  }

  public HashMap<Symbol, Type> getFields() {
    return this.fields;
  }

  public boolean putMethod(Symbol symbol, MethodTable method) {
    if (this.methods.get(symbol) == null) {
      this.methods.put(symbol, method);
      return true;
    } 
    return false;
  }

  public MethodTable getMethod(Symbol symbol) {
    return this.methods.get(symbol);
  }

  public HashMap<Symbol, MethodTable> getMethods() {
    return this.methods;
  }
  
  public String toString() {
    return this.id;
  }

}
