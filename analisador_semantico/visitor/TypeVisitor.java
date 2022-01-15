package analisador_semantico.visitor;

import analisador_semantico.syntaxtree.*;

public interface TypeVisitor {
  public Type visit(Program n); //done
  public Type visit(MainClass n); //done
  public Type visit(ClassDeclSimple n); //done
  public Type visit(ClassDeclExtends n); //done
  public Type visit(VarDecl n); //done
  public Type visit(MethodDecl n); //done
  public Type visit(Formal n); //done
  public Type visit(IntArrayType n); //done
  public Type visit(BooleanType n); //done
  public Type visit(IntegerType n); //done
  public Type visit(IdentifierType n); //done
  public Type visit(Block n); //done
  public Type visit(If n); //done
  public Type visit(While n); //done
  public Type visit(Print n); //done
  public Type visit(Assign n); //done
  public Type visit(ArrayAssign n); //done
  public Type visit(And n); //done
  public Type visit(LessThan n); //done
  public Type visit(Plus n); //done
  public Type visit(Minus n); //done 
  public Type visit(Times n); //done
  public Type visit(ArrayLookup n);
  public Type visit(ArrayLength n); //done
  public Type visit(Call n); //done
  public Type visit(IntegerLiteral n);//done
  public Type visit(True n); //done
  public Type visit(False n); //done
  public Type visit(IdentifierExp n); //done
  public Type visit(This n); //done
  public Type visit(NewArray n); //done
  public Type visit(NewObject n); //done
  public Type visit(Not n); //done
  public Type visit(Identifier n); //done
}
