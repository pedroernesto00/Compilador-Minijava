package analisador_semantico.syntaxtree;
import analisador_semantico.visitor.*;
import traducao.visitor.VisitorIR;

public class Block extends Statement {
  public StatementList sl;

  public Block(StatementList asl) {
    sl=asl;
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

