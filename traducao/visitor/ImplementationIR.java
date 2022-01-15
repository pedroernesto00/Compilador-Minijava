package traducao.visitor;

import traducao.FragAux.Exp;
import traducao.FragAux.Frag;
import traducao.FragAux.ProcFrag;
import traducao.Frame.*;
import traducao.Tree.*;
import analisador_semantico.syntaxtree.*;
import analisador_semantico.syntaxtree.Print;
import analisador_semantico.tables.*;
import analisador_semantico.tables.Symbol;

import java.util.*;
import traducao.Temp.*;

public class ImplementationIR implements VisitorIR {
  private Frame current_frame;
  public Frag frag_head, frags;
  public ClassTable current_class;
  public ProgramTable program;
  public MethodTable current_method;

  public ImplementationIR(Frame frame, ProgramTable program) {
    this.current_frame = frame;
    this.frags = new Frag(null);
    this.frag_head = frags;
    this.program = program;
  }

  public Exp visit(Program n){ //done
    n.m.accept(this);
    for (int i = 0; i < n.cl.size() ; i++) {
        n.cl.elementAt(i).accept(this);
    }   

    return null;
  }

  public Exp visit(MainClass n){ //done
    this.current_class = this.program.getClass(Symbol.symbol(n.i1.s));
		ArrayList<Boolean> escape_list = new ArrayList<Boolean>();
		escape_list.add(false);
		current_frame = current_frame.newFrame(Symbol.symbol("main"), escape_list);
    
    Stm body = new EXPR(n.s.accept(this).unEx());
    ArrayList<Stm> stm_list = new ArrayList<Stm>();
    stm_list.add(body);

    current_frame.procEntryExit1(stm_list);
    frags.addNext(new ProcFrag(body, current_frame));
    frags = frags.getNext();

    return null;
  }

  public Exp visit(ClassDeclSimple n){ //done
    this.current_class = this.program.getClass(Symbol.symbol(n.i.s));
    n.i.accept(this);
    int i;

    for (i = 0; i < n.vl.size() ; i++) {
        n.vl.elementAt(i).accept(this);            
    }
    for (i = 0; i < n.ml.size(); i++) {
        n.ml.elementAt(i).accept(this);            
    }

    return null;
  }

  public Exp visit(ClassDeclExtends n){ //done
    this.current_class = this.program.getClass(Symbol.symbol(n.i.s));
    n.i.accept(this);
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

  public Exp visit(VarDecl n){ //done
    return null;
  }
  
  public Exp visit(MethodDecl n){ //done
    Stm body = new EXPR(new CONST(0));
    ArrayList<Boolean> escape_list = new ArrayList<Boolean>();

    this.current_method = this.current_class.getMethod(Symbol.symbol(n.i.s));
		
    for (int i = 0; i <= n.fl.size(); i++) {
			escape_list.add(false);
		}

    current_frame = current_frame.newFrame(Symbol.symbol(current_class.toString() + "$" + current_method.toString()), escape_list);

    for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);	
		}

    for (int i = 0; i < n.sl.size(); i++) {
			body = new SEQ(body,new EXPR(n.sl.elementAt(i).accept(this).unEx()));
		}        
    
    ArrayList<Stm> stm_list = new ArrayList<Stm>();
		stm_list.add(body);
		current_frame.procEntryExit1(stm_list);
		frags.addNext(new ProcFrag(body,current_frame));
    frags = frags.getNext();
		current_method = null;

    return null;
  }

  public Exp visit(Formal n){ //done
    current_frame.allocLocal(false);
    return null;
  }

  public Exp visit(IntArrayType n){//done
    return null;
  }

  public Exp visit(BooleanType n){//done
    return null;
  }

  public Exp visit(IntegerType n){//done
    return null;
  }

  public Exp visit(IdentifierType n){ //done
    return null;
  }

  public Exp visit(Block n){ //done
    traducao.Tree.Exp stm = new CONST(0);
    int i;

    for(i = 0; i < n.sl.size(); i++) {
      EXPR expr = new EXPR(n.sl.elementAt(i).accept(this).unEx());
      SEQ seq = new SEQ(new EXPR(stm), expr);
      stm = new ESEQ(seq, new CONST(0));
    }

    return new Exp(stm);
  }

  public Exp visit(If n){ //done
    Exp exp = n.e.accept(this);
    Stm stm1 = new EXPR(n.s1.accept(this).unEx());
    Stm stm2 = new EXPR(n.s2.accept(this).unEx());

    Label t = new Label(), f = new Label(), endif = new Label();

    SEQ falseStm = new SEQ(stm2, new LABEL(f));
    SEQ trueStm = new SEQ(stm1, new LABEL(t));
    SEQ seqTrueFalse = new SEQ(trueStm, falseStm);

    CJUMP cjump = new CJUMP(CJUMP.GT, exp.unEx(), new CONST(1), t, f);
    SEQ cond = new SEQ(new LABEL(endif), cjump);
    SEQ secondSeq = new SEQ(cond, seqTrueFalse);
    SEQ mainSeq = new SEQ(secondSeq, new LABEL(endif));

    return new Exp(new ESEQ(mainSeq, new CONST(0)));
  }

  public Exp visit(While n){ //done
    traducao.Tree.Exp exp = n.e.accept(this).unEx();
    Stm stm = new EXPR(n.s.accept(this).unEx());

    Label loop = new Label(), done = new Label(), body = new Label();

    SEQ bodyStm = new SEQ(stm, new LABEL(body));
    JUMP jump = new JUMP(done);
    SEQ seqExec = new SEQ(bodyStm, jump);
    CJUMP cjump = new CJUMP(CJUMP.GT, exp, new CONST(1), body, done);
                                                                    
    SEQ seqTeste = new SEQ(new LABEL(loop), cjump);
    SEQ secondSeq = new SEQ(seqTeste, seqExec);
    SEQ mainSeq = new SEQ(secondSeq, new LABEL(done));

    return new Exp(new ESEQ(mainSeq, new CONST(0)));
  }

  public Exp visit(Print n){ //done
    Exp e = n.e.accept(this);
    
    List<traducao.Tree.Exp> exps = new LinkedList<traducao.Tree.Exp>();
    exps.add(e.unEx());
    traducao.Tree.Exp exp = current_frame.externalCall("print", exps);

    return new Exp(exp);
  }

  public Exp visit(Assign n){ //done
    Exp i = n.i.accept(this);
    Exp e = n.e.accept(this);

    MOVE set = new MOVE(i.unEx(), e.unEx());
    ESEQ eseq = new ESEQ(set, new CONST(0));

    return new Exp(eseq);
  }

  public Exp visit(ArrayAssign n){ //done
    Exp i = n.i.accept(this);
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);

    BINOP mult = new BINOP(BINOP.MUL, e1.unEx(), new CONST(current_frame.wordSize()));
    BINOP sum = new BINOP(BINOP.PLUS, i.unEx(), mult);
    MEM mem = new MEM(sum);
    MOVE move = new MOVE(mem, e2.unEx());

    return new Exp(new ESEQ(move, new CONST(0)));
  }

  public Exp visit(And n){ //done
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);

    BINOP binop = new BINOP(BINOP.AND, e1.unEx(), e2.unEx());

    return new Exp(binop);
  }

  public Exp visit(LessThan n){ //done
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);

    BINOP diff = new BINOP(BINOP.MINUS, e2.unEx(), e1.unEx());

    return new Exp(diff);
  }

  public Exp visit(Plus n){ //done
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);

    BINOP binop = new BINOP(BINOP.PLUS, e1.unEx(), e2.unEx());

    return new Exp(binop);    
  }

  public Exp visit(Minus n){ //done
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);

    BINOP binop = new BINOP(BINOP.MINUS, e1.unEx(), e2.unEx());

    return new Exp(binop);
  }

  public Exp visit(Times n){ //done
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);

    BINOP binop = new BINOP(BINOP.MUL, e1.unEx(), e2.unEx());

    return new Exp(binop);
  }

  public Exp visit(ArrayLookup n){ //done
    Exp e1 = n.e1.accept(this);
    Exp e2 = n.e2.accept(this);
  
    BINOP sum = new BINOP(BINOP.PLUS, new CONST(1), e2.unEx());
    BINOP mult = new BINOP(BINOP.MUL, sum, new CONST(current_frame.wordSize()));
    BINOP sum2 = new BINOP(BINOP.PLUS, e1.unEx(), mult);

    return new Exp(new MEM(sum2));
  }

  public Exp visit(ArrayLength n){ //done
    Exp e = n.e.accept(this);
    return new Exp(new MEM(e.unEx()));
  }

  public Exp visit(Call n){ //done
    ClassTable aux_class = null;
    String class_name = null;

		traducao.Tree.ExpList lista_exp = null;
		for (int i = n.el.size() -1; i >= 0 ; i--) {
			lista_exp = new traducao.Tree.ExpList(n.el.elementAt(i).accept(this).unEx(),lista_exp);
		}

    lista_exp = new traducao.Tree.ExpList(n.e.accept(this).unEx(), lista_exp);

    if (n.e instanceof This) {
      aux_class = this.current_class;
    }

    if (n.e instanceof IdentifierExp) {
      Symbol symbol = Symbol.symbol(((IdentifierExp)n.e).s);

      if (this.current_method.getLocal(symbol) instanceof IdentifierType) {
        Type type = this.current_method.getLocal(symbol);
        class_name = ((IdentifierType)type).s;
      }

      else if (this.current_method.getParam(symbol) instanceof IdentifierType) {
        Type type = this.current_method.getParam(symbol);
        class_name = ((IdentifierType)type).s;
      }

      else if (this.current_class.getField(symbol) instanceof IdentifierType) {
        Type type = this.current_class.getField(symbol);
        class_name = ((IdentifierType)type).s;
      }      
    }

    if (n.e instanceof Call) {
      aux_class = this.current_class;
      ClassTable aux_class2 = this.program.getClass(Symbol.symbol(aux_class.toString()));
      MethodTable aux_method = aux_class2.getMethod(Symbol.symbol(n.i.s));
      aux_class = this.program.getClass(Symbol.symbol(aux_method.class_id));
    }

    if (n.e instanceof NewObject) {
      aux_class = this.program.getClass(Symbol.symbol(((NewObject)n.e).i.s));
    }

    if (!(aux_class == null)) {
      class_name = aux_class.toString();
    }

    return new Exp(new CALL(new NAME(
      new Label(class_name+"$"+n.i.toString())), lista_exp));
		    
  }

  public Exp visit(IntegerLiteral n){  //done
    CONST constant = new CONST(n.i);

    return new Exp(constant);
  }
  
  public Exp visit(True n){ //done

    CONST constant = new CONST(1);
    return new Exp(constant);
  }

  public Exp visit(False n){ //done

    CONST constant = new CONST(0);
    return new Exp(constant);

  }

  public Exp visit(IdentifierExp n){ //done
    Access a = current_frame.allocLocal(false);
    return new Exp(a.exp(new TEMP(current_frame.FP())));
  }
  
  public Exp visit(This n){ //done
    return new Exp(new MEM(new TEMP(current_frame.FP())));
  }

  public Exp visit(NewArray n){ //done
    Exp e = n.e.accept(this);
    BINOP sum = new BINOP(BINOP.PLUS, e.unEx(), new CONST(1));
    traducao.Tree.Exp exp = new BINOP(BINOP.MUL, sum, new CONST(current_frame.wordSize()));

    List<traducao.Tree.Exp> params = new LinkedList<traducao.Tree.Exp>();
    params.add(exp);
    traducao.Tree.Exp return_exp = current_frame.externalCall("initArray", params); 

    return new Exp(return_exp);
  }

  public Exp visit(NewObject n){ //done
    ClassTable classe = this.program.getClass(Symbol.symbol(n.i.s));
    int size = classe.getFields().keySet().size();

    List<traducao.Tree.Exp> params = new LinkedList<traducao.Tree.Exp>();
    BINOP binop = new BINOP(BINOP.MUL, new CONST(1+size), new CONST(current_frame.wordSize()));
    params.add(binop);

    traducao.Tree.Exp return_exp = current_frame.externalCall("malloc", params); 

    return new Exp(return_exp);
  }
  
  public Exp visit(Not n){ //done
    Exp e =  n.e.accept(this);

    BINOP binop = new BINOP(BINOP.MINUS, new CONST(1), e.unEx());

    return new Exp(binop);

  }

  public Exp visit(Identifier n){ //done
    Access a = current_frame.allocLocal(false);
    return new Exp(a.exp(new TEMP(current_frame.FP())));
  }
}
