package Assem;

import traducao.Frame.Frame;
import traducao.Temp.*;
import traducao.Tree.*;

public class Codegen {
  Frame frame;
  private InstrList init = null, last = null;

  public Codegen(Frame f) {
    frame = f;
  }
  
  private void emit(Instr instruction) {
    if (!(last == null)) {
      last = init.tail = new InstrList(instruction, null);
    }
    else {
      last = init = new InstrList(instruction, null);
    }
  }

  void munchStm(Stm stm) {
    if (stm instanceof SEQ) {
      munchStm(((SEQ)stm).left);
      munchStm(((SEQ)stm).right);
    }

    else if (stm instanceof traducao.Tree.MOVE) {
      munchMove(((traducao.Tree.MOVE)stm).dst, ((traducao.Tree.MOVE)stm).src);
    }
    
    else if (stm instanceof traducao.Tree.LABEL) {
      emit(new LABEL(((traducao.Tree.LABEL)stm).label + ":\n", ((traducao.Tree.LABEL)stm).label));
    }

    else if (stm instanceof JUMP) {
      munchJump((JUMP)stm);
    }

    else if (stm instanceof CJUMP) {
      munchCJump((CJUMP)stm);
    }

    else if (stm instanceof EXPR) {
      if (((EXPR)stm).exp instanceof CALL) {
        CALL call = (CALL)(((EXPR)stm).exp); 
        Temp temp = munchExp(call.func);
        TempList temp_list = munchArgs(0, call.args);
        NAME name = (NAME) (call.func);
        emit(new OPER("jal " + name.label + "\n", null, 
        new TempList(temp, temp_list)));
      }
    }
  }

  Temp munchExp(Exp exp) {
    if (exp instanceof MEM) {
      return munchMEM((MEM)exp);
    }

    if (exp instanceof CONST) {
      Temp temp = new Temp();
      emit(new OPER("li `d0," + ((CONST)exp).value + "\n", new TempList(temp,null), null));
      return temp;
    }

    if (exp instanceof BINOP) {
      return munchBinop((BINOP)exp);
    }

    if (exp instanceof TEMP) {
      return ((TEMP)exp).temp;
    }

    if (exp instanceof NAME){
      return new Temp();
    }

    return null;
  }

  private TempList munchArgs(int i, ExpList args) {
    ExpList temp = args;
    TempList ret = null;

    while (!(temp == null)) {
      Temp temp_head = munchExp(temp.head);
      ret = new TempList(temp_head, ret);
      temp = temp.tail;
    }

    return ret;
  }

  private void munchCJump(CJUMP s) {
    String relop = "";
    switch (s.relop) {
      case CJUMP.EQ:
        relop = "beq";
        break;
      case CJUMP.NE:
        relop = "bne";
        break;
      case CJUMP.GT:
        relop = "bgt";
        break;
      case CJUMP.GE:
        relop = "bge";
        break;
      case CJUMP.LT:
        relop = "blt";
        break;
      case CJUMP.LE:
        relop = "ble";
        break;
    }

    Temp l = munchExp(s.left);
    Temp r = munchExp(s.right);
    emit(new OPER(relop + "`s0, `s1, `j0\n", null, new TempList(l, new TempList(r, null)),
                  new LabelList(s.iftrue, new LabelList(s.iffalse, null))));
  }

  private void munchJump(JUMP s) {
    NAME name = ((NAME) s.exp);
    emit(new OPER("j `j0\n", null, null, new LabelList(name.label, null)));
  }

  void munchMove(MEM dst, Exp src) {
    if (dst.exp instanceof BINOP) {
      if (((BINOP) dst.exp).binop == BINOP.PLUS) {
        if (((BINOP) dst.exp).right instanceof CONST) {
          Temp temp1 = munchExp(((BINOP) dst.exp).left);
          Temp temp2 = munchExp(src);
          TempList s = new TempList(temp2, null);
          TempList d = new TempList(temp1, null);
          emit(new OPER("sw `s0, `d0\n", d, s));    
        } else if (((BINOP) dst.exp).left instanceof CONST){
          Temp temp2 = munchExp(src);
          TempList s = new TempList(temp2, null);
          Temp temp1 = munchExp(((BINOP) dst.exp).right);
          TempList d = new TempList(temp1, null);    
          emit(new OPER("sw `s0, `d0\n", d, s));     
        }
      }
    }
    else if (src instanceof MEM) {
      Temp temp1 = munchExp(dst.exp);
      TempList d = new TempList(temp1, null);
      Temp temp2 = munchExp((MEM)src);
      TempList s = new TempList(temp2, null);

      emit(new OPER("move `s0, `d0\n", d, s));
    }
    else if (dst.exp instanceof CONST) {
      Temp temp1 = munchExp((CONST)dst.exp);
      TempList d = new TempList(temp1, null);
      Temp temp2 = munchExp(src);
      TempList s = new TempList(temp2, null);

      emit(new OPER("sw, `s0, `d0\n", d, s));
    }
    else {
      TempList d = new TempList(munchExp(dst.exp), null);
      TempList s = new TempList(munchExp(src), null);
      emit(new OPER("sw `s0, 0(`s0)\n", d, s));
    }

  }

  void munchMove(TEMP dst, Exp src) {
    Temp temp = munchExp(src);

    emit(new MOVE("move `s0, `d0", dst.temp, temp));
  }

  void munchMove(Exp dst, Exp src) {
    if (dst instanceof MEM) {
      munchMove((MEM) dst, src);
    }
    else if (dst instanceof TEMP) {
      if (src instanceof CALL) {
        Temp temp = munchExp(((CALL) src).func);
        TempList templist = munchArgs(0, ((CALL)src).args);
        Label funcname = ((NAME)((CALL) src).func).label;
        emit(new OPER("jal " + funcname.toString() + "\n", new TempList(temp, null), 
                      templist));
      }
    }
  }

  private Temp munchBinop(BINOP b){
    Temp temp = new Temp();
    TempList d = new TempList(temp, null);

  if (b.binop == BINOP.PLUS){
    if (b.left instanceof CONST){
      TempList s = new TempList(munchExp(b.right),null);
      CONST c = ((CONST)b.left);
      emit(new OPER("addi `d0, `s0,"+ c.value+ "\n", d, s));

    }
    else if (b.right instanceof CONST){
      TempList s = new TempList(munchExp(b.left),null);
      CONST c = ((CONST)b.right);
      emit(new OPER("addi `d0, `s0,"+ c.value+ "\n", d, s)); 

  }
  else{
    TempList s = new TempList(munchExp(b.left),new TempList(munchExp(b.right),null));
    emit(new OPER("add `d0, `s0, `s1 \n",d,s));
  }
  }
  else if (b.binop == BINOP.MINUS){
    TempList s = new TempList(munchExp(b.left),new TempList(munchExp(b.right),null));
    emit(new OPER("sub `d0, `s0, `s1 \n",d,s));
 
  }

  else if (b.binop == BINOP.MUL){
    TempList s = new TempList(munchExp(b.left),new TempList(munchExp(b.right),null));
    emit(new OPER("mul `d0, `s0, `s1 \n",d,s));

  }
  else if (b.binop == BINOP.DIV){
    TempList s = new TempList(munchExp(b.left),new TempList(munchExp(b.right),null));
    emit(new OPER("div `s0,`s1\nmflo `d0\n",d,s));

  }
  else if (b.binop == BINOP.AND){
    TempList s = new TempList(munchExp(b.left),new TempList(munchExp(b.right),null));
    emit(new OPER("and `d0, `s0, `s1 \n",d,s));
  }
  else if (b.binop == BINOP.OR){
    TempList s = new TempList(munchExp(b.left),new TempList(munchExp(b.right),null));
    emit(new OPER("or `d0, `s0, `s1 \n",d,s));
  }
    return temp;
  }

  private Temp munchMEM(MEM m){
    
    Temp temp = new Temp();
    TempList d = new TempList(temp, null);

    if (m.exp instanceof BINOP && ((BINOP)m.exp).binop == BINOP.PLUS){
      Exp r = ((BINOP)m.exp).right;
      Exp l = ((BINOP)m.exp).left;
      
      if(r instanceof CONST){
        TempList s = new TempList(munchExp(l),null);
        emit(new OPER("lw `d0, " + ((CONST)r).value + "(`s0)\n", d ,s));
      }
      else if(l instanceof CONST){
        TempList s = new TempList(munchExp(r),null);
        emit(new OPER("lw `d0, "+((CONST)l).value+"(`s0) \n",d ,s));
      }

    }
    else if (m.exp instanceof CONST){
      CONST c = (CONST)m.exp;
      emit(new OPER("lw `d0, "+c.value+"($zero) \n", d ,null));
    }
    else{
      emit(new OPER("lw `d0, `s0\n", d,new TempList(munchExp(m.exp),null)));
    }

    return temp;
  }

  public InstrList codegen(Stm s){
    InstrList list;
    munchStm(s);
    list = init;
    init = last = null;
    return list;
  }
}

