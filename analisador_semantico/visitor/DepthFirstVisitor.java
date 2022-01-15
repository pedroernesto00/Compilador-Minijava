package analisador_semantico.visitor;
import analisador_semantico.syntaxtree.*;
import analisador_semantico.tables.*;
import java.util.*;

public class DepthFirstVisitor implements Visitor{
  private ClassTable current_class = null;
  private MethodTable current_method;
  private ProgramTable current_program;
  private ErrorMsg error;
  
  public DepthFirstVisitor() {
    current_program = new ProgramTable("program");
    error = new ErrorMsg();
  }

  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.elementAt(i).accept(this);
    }
  }

  public void visit(MainClass n) {
    n.i1.accept(this);

    ClassTable main_class = new ClassTable(n.i1.s, null);

    if (!(current_program.putClass(Symbol.symbol(n.i1.s), main_class))) {
      error.complain("A classe " + n.i1.s + " já existe.");
    } else {current_class = main_class;}

    MethodTable main_method = new MethodTable("main", null, n.i1.s);

    if (!(current_class.putMethod(Symbol.symbol("main"), main_method))) {
      error.complain("O método main já existe.");
    } else {
      current_method = main_method;
      main_method.putParam(Symbol.symbol(n.i2.s), null);
    }

    n.i2.accept(this);
    n.s.accept(this);
  }

  public void visit(ClassDeclSimple n) {
    n.i.accept(this);
    ClassTable declared_class = new ClassTable(n.i.s, null);

    if (!(current_program.putClass(Symbol.symbol(n.i.s), declared_class))) {
      error.complain("A classe " + n.i.s + " já existe.");
    } else {
      current_class = declared_class;
      current_method = null;
    }

    int i;
    for (i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }
  }

  public void visit(ClassDeclExtends n) {
    n.i.accept(this); n.j.accept(this);

    ClassTable declared_class = new ClassTable(n.i.s, n.j.s);
    ClassTable parent_class = current_program.getClass(Symbol.symbol(n.j.s));

    if (!(current_program.putClass(Symbol.symbol(n.i.s), declared_class))) {
      error.complain("A classe " + n.i.s + " já existe.");
    } else {
      current_class = declared_class;
      current_method = null;
    }

    int i;
    for (i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    Set parent_methods = parent_class.getMethods().keySet();
    Set parent_fields = parent_class.getFields().keySet();

    Iterator it_m = parent_methods.iterator();
    Iterator it_f = parent_fields.iterator();

    while (it_m.hasNext()) {
      Symbol symbol_method = Symbol.symbol(it_m.next().toString());
      MethodTable method = parent_class.getMethod(symbol_method);

      current_class.putMethod(symbol_method, method);
    }
    
    while (it_f.hasNext()) {
      Symbol symbol_field = Symbol.symbol(it_f.next().toString());
      Type field_type = parent_class.getField(symbol_field);

      current_class.putField(symbol_field, field_type);
    }
  }
  
  public void visit(VarDecl n) {
    n.t.accept(this); n.i.accept(this);

    if (!(current_method == null)) {
      if (!(current_method.putLocal(Symbol.symbol(n.i.s), n.t))) {
        error.complain("A variável " + n.i.s + " já existe no método " 
        + current_method.toString() + ".");
      }
    } else {
      if (!(current_class.putField(Symbol.symbol(n.i.s), n.t))) {
        error.complain("A variável " + n.i.s + " já exista na classe "
        + current_class.toString() + ".");
      }
    }
  }

  public void visit(MethodDecl n) {
    n.t.accept(this); n.i.accept(this);

    MethodTable declared_method = new MethodTable(n.i.s, n.t, 
    current_class.toString());

    if (!(current_class.putMethod(Symbol.symbol(n.i.s), 
    declared_method))) {
      error.complain("O método " + declared_method.toString() + " já existe na classe "
      + current_class.toString() + ".");
    } else {
      current_method = declared_method;
    }

    int i;

    for (i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }

    for (i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }

    n.e.accept(this);
  }

  public void visit(Formal n) {
    n.t.accept(this);
    n.i.accept(this);

    if (!(current_method.putParam(Symbol.symbol(n.i.s), n.t))) {
      error.complain("O parâmetro " + n.i.s 
      + " já existe no método" + current_method.toString() +".");
    }
  }

  public void visit(IntArrayType n) {}
  public void visit(BooleanType n) {}
  public void visit(IntegerType n) {}
  public void visit(IdentifierType n) {}

  public void visit(Block n) {
    int i;
    for (i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
  }

  public void visit(If n) {
    n.e.accept(this); n.s1.accept(this); n.s2.accept(this);
  }

  public void visit(While n) {
    n.e.accept(this); n.s.accept(this);
  }

  public void visit(Print n) {
    n.e.accept(this);
  }

  public void visit(Assign n) {
    n.i.accept(this); n.e.accept(this);
  }

  public void visit(ArrayAssign n) {
    n.i.accept(this); n.e1.accept(this); n.e2.accept(this);
  }
  
  public void visit(And n) {
    n.e1.accept(this); n.e2.accept(this);
  }

  public void visit(LessThan n) {
    n.e1.accept(this); n.e2.accept(this);
  }

  public void visit(Plus n) {
    n.e1.accept(this); n.e2.accept(this);
  }

  public void visit(Minus n) {
    n.e1.accept(this); n.e2.accept(this);
  }

  public void visit(Times n) {
    n.e1.accept(this); n.e2.accept(this);
  }

  public void visit(ArrayLookup n) {
    n.e1.accept(this); n.e2.accept(this);
  }
  
  public void visit(ArrayLength n) {
    n.e.accept(this);
  }

  public void visit(Call n) {
    n.i.accept(this);

    int i;
    for (i = 0; i < n.el.size(); i++) {
      n.el.elementAt(i).accept(this);
    }
  }

  public void visit(IntegerLiteral n) {}
  public void visit(True n) {}
  public void visit(False n) {}

  public void visit(IdentifierExp n) {}
  public void visit(This n) {}

  public void visit(NewArray n) {
    n.e.accept(this);
  }

  public void visit(NewObject n) {
    n.i.accept(this);
  }

  public void visit(Not n) {
    n.e.accept(this);
  }

  public void visit(Identifier n) {}

  public ProgramTable program_table() {
    return this.current_program;
  }
}
