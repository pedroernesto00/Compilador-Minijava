package analisador_semantico.visitor;

import analisador_semantico.tables.*;
import analisador_semantico.syntaxtree.*;
import java.util.*;

public class TypeDepthFirstVisitor implements TypeVisitor {
    
    private ErrorMsg error = new ErrorMsg();
    private ClassTable current_class;
    private MethodTable current_method;
    private ProgramTable current_program;

    public TypeDepthFirstVisitor(ProgramTable p) {
        this.current_program = p;
    }

    public Type visit(Program n) {
        n.m.accept(this);
        for (int i = 0; i < n.cl.size() ; i++) {
            n.cl.elementAt(i).accept(this);
        }   

        return null;
    }

    public Type visit(MainClass n) {
        n.i1.accept(this);
        current_class = current_program.getClass(Symbol.symbol(n.i1.s));
        n.i2.accept(this);
        current_method = current_class.getMethod(Symbol.symbol(n.i2.s));
        n.s.accept(this);

        return null;
    }

    public Type visit(ClassDeclSimple n) {
        n.i.accept(this);
        current_class = current_program.getClass(Symbol.symbol(n.i.s));
        current_method = null;
        int i;
        for (i = 0; i < n.vl.size() ; i++) {
            n.vl.elementAt(i).accept(this);            
        }
        for (i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);            
        }

        return new IdentifierType(n.i.s);
    }

    public Type visit(ClassDeclExtends n) {
        n.i.accept(this);
        current_class = current_program.getClass(Symbol.symbol(n.i.s));
        current_method = null;
        n.j.accept(this);
        int i;
        for (i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }

        return null;
    }

    public Type visit(VarDecl n) {
        n.t.accept(this);
        n.i.accept(this);

        return null;
    }

    public Type visit(MethodDecl n) {
        n.t.accept(this);
        n.i.accept(this);
        current_method = current_class.getMethod(Symbol.symbol(n.i.s));
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
        if (!(n.e.accept(this).getClass().equals(n.t.getClass()))) {
            error.complain("Tipo do retorno " + n.i.s + " não condiz com o tipo do método.");
        }

        return null;
    }

    public Type visit(Formal n) {
        n.t.accept(this);
        n.i.accept(this);

        return null;
    }

    public Type visit(IntArrayType n) {
        return new IntArrayType();
    }

    public Type visit(BooleanType n) {
        return new BooleanType();
    }

    public Type visit(IntegerType n) {
        return new IntegerType();
    }

    public Type visit(IdentifierType n) {
        return n;
    }

    public Type visit(Block n) {
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.elementAt(i).accept(this);
        }

        return null;
    }

    public Type visit(If n) {
        if (!(n.e.accept(this) instanceof BooleanType)) {
            error.complain("A expressão IF deve conter booleanos.");
        }

        n.s1.accept(this);
        n.s2.accept(this);
        
        return null;
    }

    public Type visit(While n) {
        if (!(n.e.accept(this) instanceof BooleanType)) {
            error.complain("A expressão WHILE deve conter um booleano.");
        }
        n.s.accept(this);
        
        return null;
    }

    public Type visit(Print n) {
        n.e.accept(this);

        return null;
    }

    public Type visit(Assign n) {
        if (!(current_method == null)) {
            if(!(current_method.getLocal(Symbol.symbol(n.i.s)) == null)) {
                if (!(n.i.accept(this).getClass().equals(n.e.accept(this).getClass()))) {
                    error.complain("Tipos não compatíveisAssign1.");
                }
            } 

            else if(!(current_method.getParam(Symbol.symbol(n.i.s)) == null)) {
                if (!(n.i.accept(this).getClass().equals(n.e.accept(this).getClass()))) {
                    error.complain("Tipos não compatíveisAssign2.");
                }
            } 
        }

        else if (!(current_class == null)) {
            if (!(current_class.getField(Symbol.symbol(n.i.s)) == null)) {
                if (!(n.i.accept(this).getClass().equals(n.e.accept(this).getClass()))) {
                    error.complain("Tipos não compatíveisAssign3.");
                }
            }
        }


        return null;
    }


    public Type visit(ArrayAssign n) {
        Type type;
        type = null;

        if (!(current_method == null)) {
            if (!(current_method.getParam(Symbol.symbol(n.i.s)) == null)) {
                type = current_method.getParam(Symbol.symbol(n.i.s));
            }

            else if (!(current_method.getLocal(Symbol.symbol(n.i.s)) == null)) {
                type = current_method.getLocal(Symbol.symbol(n.i.s));
            }
        }
        
        if (!(current_class == null) && type == null) {
            if (!(current_class.getField(Symbol.symbol(n.i.s)) == null)) {
                type = current_class.getField(Symbol.symbol(n.i.s));
            }  
        }

        if (type == null) {
            error.complain("Array " + n.i.s + " não declarado. " + current_method.toString());
        }

        if (!(n.e1.accept(this) instanceof IntegerType)) {
            error.complain("O índice do array precisa ser inteiro.");
        }

        if (!(n.e2.accept(this) instanceof IntegerType)) {
            error.complain("O valor precisa ser inteiro.");
        }

        if (!(type instanceof IntArrayType)) {
            error.complain("O array precisa ser de inteiros.");
        }

        return null;
    }

    public Type visit(And n) {
        if (!(n.e1.accept(this) instanceof BooleanType && n.e2.accept(this) instanceof BooleanType)) {
            error.complain("A expressão AND (&&) deve ser formada por dois booleanos.");
        }

        return new BooleanType();
    }

    public Type visit(LessThan n) {
        if (!(n.e1.accept(this) instanceof IntegerType && n.e2.accept(this) instanceof IntegerType)) {
            error.complain("A expressão LESS THAN (<) deve ser formada por dois inteiros.");
        }

        return new BooleanType();
    }

    public Type visit(Plus n) {
        if (!(n.e1.accept(this) instanceof IntegerType && n.e2.accept(this) instanceof IntegerType)) {
            error.complain("A expressão PLUS (+) deve ser formada por dois inteiros.");
        }

        return new IntegerType();
    }

    public Type visit(Minus n) {
        if (!(n.e1.accept(this) instanceof IntegerType && n.e2.accept(this) instanceof IntegerType)) {
            error.complain("A expressão MINUS (-) deve ser formada por dois inteiros.");
        }

        return new IntegerType();
    }

    public Type visit(Times n) {
        if (!(n.e1.accept(this) instanceof IntegerType && n.e2.accept(this) instanceof IntegerType)) {
            error.complain("A expressão TIMES (*) deve ser formada por dois inteiros.");
        }

        return new IntegerType();
    }

    public Type visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);

        return new IntegerType();
    }

    public Type visit(ArrayLength n) {
        n.e.accept(this);
        return new IntegerType();
    }

    public Type visit(Call n) {
        Type type = n.e.accept(this), expected_type;
        ClassTable class2;
        MethodTable method2;
        Exp exp;
        
        if ((!(type instanceof IdentifierType) || type == null)) {
            error.complain("A classe não existe.");
        } 
        
        IdentifierType idType = (IdentifierType) type;

        class2 = current_program.getClass(Symbol.symbol(idType.s));
        if (class2.getMethod(Symbol.symbol(n.i.s)) == null) {
            error.complain("Método " + n.i.s +" não existe na classe " + 
            class2.toString() + ".");
        }    
        
        method2 = class2.getMethod(Symbol.symbol(n.i.s));
        
        if (!(n.el.size() == method2.ParamsSize())) {
            error.complain("Quantidade de parâmetros não compatíveis.");
        }
        
        ArrayList<Type> methods_params = method2.getParamsTypes();
        
        for (int i = 0; i < n.el.size(); i++) {
            exp = n.el.elementAt(i);
            type = exp.accept(this);
            expected_type = methods_params.get(i);

            if (!(type.getClass().equals(expected_type.getClass()))) { 
                error.complain("Tipos não compatíveis.");
            }
        }

        return method2.getType();

    }

    public Type visit(IntegerLiteral n) {
        return new IntegerType();
    }

    public Type visit(True n) {
        return new BooleanType();
    }

    public Type visit(False n) {
        return new BooleanType();
    }

    public Type visit(IdentifierExp n) {
        Type type = null;

        if (!(current_method == null)) {
            if (!(current_method.getParam(Symbol.symbol(n.s)) == null)) {
                type = current_method.getParam(Symbol.symbol(n.s));
            }
            if (!(current_method.getLocal(Symbol.symbol(n.s)) == null)) {
    
                type = current_method.getLocal(Symbol.symbol(n.s));
            }
        }
        if (!(current_class == null)) {

            if (!(current_class.getField(Symbol.symbol(n.s)) == null)) {
                type = current_class.getField(Symbol.symbol(n.s));
            }
        }

        if (!(current_program.getClass(Symbol.symbol(n.s)) == null)) {
            type = new IdentifierType(n.s);
        }

        if (type == null) {
            System.out.println(current_class);
            System.out.println(current_method.getLocalVariables());
            error.complain("Identificador " + n.s + " não existe.");
        }
        
        return type;
    }

    public Type visit(This n) {
        if (current_class == null) {
            error.complain("Fora do escopo da classe.");
        }

        return new IdentifierType(current_class.toString());
    }

    public Type visit(NewArray n) {
        n.e.accept(this);
        return new IntArrayType();
    }

    public Type visit(NewObject n) {
        current_class = current_program.getClass(Symbol.symbol(n.i.s));
        if (current_class == null) {
           error.complain("Classe " + n.i.s + " não existe.");
        }

        return new IdentifierType(current_class.toString());
    }

    public Type visit(Not n) {
        if (!(n.e.accept(this) instanceof BooleanType)) {
            error.complain("A expressão NOT (!) deve conter um booleano.");
        }
        return new BooleanType();
    }

    public Type visit(Identifier n) {
        Type id = null;

        if (!(current_method == null)) {
            id = current_method.getParam(Symbol.symbol(n.s));
            if (id == null) {
                id = current_method.getLocal(Symbol.symbol(n.s));
                if (!(id == null)) {
                    return id;
                }
            } else {
                return id;
            }
        }

        if (!(current_class == null)) {
            id = current_class.getField(Symbol.symbol(n.s));
            if (!(id == null)) {
                return id;
            }
        }

        if (!(current_program.getClass(Symbol.symbol(n.s)) == null)) {
            id = new IdentifierType(n.s);
            if (!(id == null)) {
                return id;
            }
        }

        return null;
    }
}
