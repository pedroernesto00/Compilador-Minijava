package Assem;

import traducao.Temp.*;
public abstract class Instr {
  public String assem;
  public abstract TempList use();
  public abstract TempList def();
  public abstract Targets jumps();

  private Temp nthTemp(TempList l, int i) {
    if (i==0) return l.head;
    else return nthTemp(l.tail,i-1);
  }

  private Label nthLabel(LabelList l, int i) {
    if (i==0) return l.head;
    else return nthLabel(l.tail,i-1);
  }

  public String format(TempMap m) {
    TempList dst = def();
    TempList src = use();
    Targets j = jumps();
    LabelList jump = (j==null)?null:j.labels;
    StringBuffer s = new StringBuffer();
    int len = assem.length();
    Temp t = null;
    
    for(int i=0; i<len; i++)
	if (assem.charAt(i)=='`')
	   switch(assem.charAt(++i)) {
      case 's': {int n = Character.digit(assem.charAt(++i),10);
       t = nthTemp(src,n);
       if (m.tempMap(t) == null) s.append(t);
       else s.append(m.tempMap(t));
			}
			break;
	      case 'd': {int n = Character.digit(assem.charAt(++i),10);
          t = nthTemp(dst,n);
          if (m.tempMap(t) == null) s.append(t);
          else s.append(m.tempMap(t));
			}
 			break;
	      case 'j': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(nthLabel(jump,n).toString());
			}
 			break;
	      case '`': s.append('`'); 
			break;
              default: throw new Error("bad Assem format");
       }
       else s.append(assem.charAt(i));

    return s.toString();
  }


}
