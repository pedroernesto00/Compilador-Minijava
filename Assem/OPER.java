package Assem;
import traducao.Temp.*;

public class OPER extends Instr {
   public TempList dst;   
   public TempList src;
   public Targets jump;

   public OPER(String a, TempList d, TempList s, LabelList j) {
      assem=a; dst=d; src=s; jump=new Targets(j);
   }
   public OPER(String a, TempList d, TempList s) {
      assem=a; dst=d; src=s; jump=null;
   }

   public TempList use() {return src;}
   public TempList def() {return dst;}
   public Targets jumps() {return jump;}

}
