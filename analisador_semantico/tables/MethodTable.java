package analisador_semantico.tables;

import java.util.*;
import analisador_semantico.syntaxtree.*;

public class MethodTable {
  public String id, class_id;
  Type t;
  HashMap<Symbol, Type> params, locals;
  private ArrayList<Type> params_type;

  public MethodTable(String id, Type type, String class_id) {
    this.id = id; this.t = type; this.class_id = class_id;
    this.params = new HashMap<Symbol, Type>();
    this.locals = new HashMap<Symbol, Type>();
    this.params_type = new ArrayList<Type>();
  }

  public boolean putParam(Symbol symbol, Type type) {
    if (this.params.get(symbol) == null) {
      this.params.put(symbol, type);
      this.params_type.add(type);
      return true;
    }
    return false;
  }

  public Type getParam(Symbol symbol) {
    return this.params.get(symbol);
  }

  public HashMap<Symbol, Type> getParams() {
    return this.params;
  }

  public ArrayList getParamsTypes() {
    return this.params_type;
  }
  
  public int ParamsSize() {
    return this.params_type.size();
  }

  public boolean putLocal(Symbol symbol, Type type) {
    if (this.locals.get(symbol) == null) {
      this.locals.put(symbol, type);
      return true;
    }
    return false;
  }

  public Type getLocal(Symbol symbol) {
    return this.locals.get(symbol);
  }

  public HashMap<Symbol, Type> getLocalVariables() {
    return this.locals;
  }

  public int LocalVariablesSize() {
    return this.locals.size();
  }

  public Type getType() {
    return this.t;
  }
  
  public String toString() {
    return this.id;
  }
}
