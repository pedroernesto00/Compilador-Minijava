package traducao.FragAux;

import traducao.Frame.*;
import traducao.Tree.*;

public class ProcFrag extends Frag {
  public Stm body;
  public Frame frame;

  public ProcFrag(Stm b, Frame f) {
    body = b;
    frame = f;
  }
}
