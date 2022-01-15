package analisador_semantico.syntaxtree;
import analisador_semantico.visitor.*;
import traducao.visitor.VisitorIR;

public class ClassDeclSimple extends ClassDecl {
  public Identifier i;
  public VarDeclList vl;  
  public MethodDeclList ml;
 
  public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml) {
    i=ai; vl=avl; ml=aml;
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
