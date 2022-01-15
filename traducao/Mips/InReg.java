package traducao.Mips;
import traducao.Temp.Temp;
import traducao.Tree.*;
import traducao.Frame.*;

public class InReg extends Access {
    Temp temp;
    InReg(Temp t) {
	temp = t;
    }

    public Exp exp(Exp fp) {
        return new TEMP(temp);
    }

    public String toString() {
        return temp.toString();
    }
}