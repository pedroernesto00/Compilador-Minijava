package dataflow_analysis.RegAlloc;
import traducao.Temp.Temp;
import dataflow_analysis.Graph.Node;
import dataflow_analysis.Graph.Graph;


abstract public class InterferenceGraph extends Graph {
   abstract public Node tnode(Temp temp);
   abstract public Temp gtemp(Node node);
   abstract public MoveList moves();
   public int spillCost(Node node) {return 1;}
}
