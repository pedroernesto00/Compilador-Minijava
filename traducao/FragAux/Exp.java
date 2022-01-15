package traducao.FragAux;

public class Exp {
  private traducao.Tree.Exp exp;

  public Exp(traducao.Tree.Exp e) {
    exp = e;
  }

  public traducao.Tree.Exp unEx() {
    return exp;
  }
}
