package traducao.FragAux;

public class Frag {
  private Frag next;

  public Frag() {
    this.next = null;
  }

  public Frag(Frag next) {
    this.next = next;
  }

  public void addNext(Frag next) {
    this.next = next;
  }

  public Frag getNext() {
    return next;
  }
}
