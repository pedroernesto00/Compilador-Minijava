package traducao.Mips;

import traducao.Frame.*;
import traducao.Tree.*;

public class InFrame extends Access {
    int offset;
    InFrame(int o) {
	offset = o;
    }

    public Exp exp(Exp fp) {
        return new MEM
	    (new BINOP(BINOP.PLUS, fp, new CONST(offset)));
    }

    public String toString() {
        Integer offset = this.offset;
	return offset.toString();
    }
}