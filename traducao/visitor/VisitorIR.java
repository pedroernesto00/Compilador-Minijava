package traducao.visitor;
import analisador_semantico.syntaxtree.*;

public interface VisitorIR {
  public traducao.FragAux.Exp visit(Program n);
  public traducao.FragAux.Exp visit(MainClass n);
  public traducao.FragAux.Exp visit(ClassDeclSimple n);
  public traducao.FragAux.Exp visit(ClassDeclExtends n);
  public traducao.FragAux.Exp visit(VarDecl n);
  public traducao.FragAux.Exp visit(MethodDecl n);
  public traducao.FragAux.Exp visit(Formal n);
  public traducao.FragAux.Exp visit(IntArrayType n);
  public traducao.FragAux.Exp visit(BooleanType n);
  public traducao.FragAux.Exp visit(IntegerType n);
  public traducao.FragAux.Exp visit(IdentifierType n);
  public traducao.FragAux.Exp visit(Block n);
  public traducao.FragAux.Exp visit(If n);
  public traducao.FragAux.Exp visit(While n);
  public traducao.FragAux.Exp visit(Print n);
  public traducao.FragAux.Exp visit(Assign n);
  public traducao.FragAux.Exp visit(ArrayAssign n);
  public traducao.FragAux.Exp visit(And n);
  public traducao.FragAux.Exp visit(LessThan n);
  public traducao.FragAux.Exp visit(Plus n);
  public traducao.FragAux.Exp visit(Minus n);
  public traducao.FragAux.Exp visit(Times n);
  public traducao.FragAux.Exp visit(ArrayLookup n);
  public traducao.FragAux.Exp visit(ArrayLength n);
  public traducao.FragAux.Exp visit(Call n);
  public traducao.FragAux.Exp visit(IntegerLiteral n);
  public traducao.FragAux.Exp visit(True n);
  public traducao.FragAux.Exp visit(False n);
  public traducao.FragAux.Exp visit(IdentifierExp n);
  public traducao.FragAux.Exp visit(This n);
  public traducao.FragAux.Exp visit(NewArray n);
  public traducao.FragAux.Exp visit(NewObject n);
  public traducao.FragAux.Exp visit(Not n);
  public traducao.FragAux.Exp visit(Identifier n);
}
