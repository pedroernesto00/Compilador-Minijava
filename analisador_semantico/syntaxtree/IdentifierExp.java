package analisador_semantico.syntaxtree;
import analisador_semantico.visitor.*;
import traducao.visitor.VisitorIR;

public class IdentifierExp extends Exp {
  public String s;
  public IdentifierExp(String as) { 
    s=as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  public traducao.FragAux.Exp accept(VisitorIR v) {
    return v.visit(this);
  }
}
