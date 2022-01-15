package traducao.Frame;
import java.util.List;
import traducao.Temp.*;
import traducao.Tree.*;
import analisador_semantico.tables.Symbol;
import Assem.InstrList;

public abstract class Frame implements TempMap {
    public Label name;
    public List<Access> formals;
    public abstract Frame newFrame(Symbol name, List<Boolean> formals);
    public abstract Access allocLocal(boolean escape);
    public abstract Temp FP();
    public abstract int wordSize();
    public abstract Exp externalCall(String func, List<Exp> args);
    public abstract Temp RV();
    public abstract String string(Label label, String value);
    public abstract Label badPtr();
    public abstract Label badSub();
    public abstract String tempMap(Temp temp);
    public abstract InstrList codegen(StmList stms);
    public abstract void procEntryExit1(List<Stm> body);
    //public abstract void procEntryExit2(List<Assem.Instr> body);
    //public abstract void procEntryExit3(List<Assem.Instr> body);
    public abstract Temp[] registers();
    //public abstract void spill(List<Assem.Instr> insns, Temp.Temp[] spills);
    public abstract String programTail(); //append to end of target code
}