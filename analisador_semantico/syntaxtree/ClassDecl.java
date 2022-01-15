package analisador_semantico.syntaxtree;
import analisador_semantico.visitor.*;
import traducao.visitor.VisitorIR;

public abstract class ClassDecl {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract traducao.FragAux.Exp accept(VisitorIR v);
}
