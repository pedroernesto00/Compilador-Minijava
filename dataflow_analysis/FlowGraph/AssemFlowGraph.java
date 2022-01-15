package dataflow_analysis.FlowGraph;

import java.util.Hashtable;

import Assem.Instr;
import Assem.InstrList;
import dataflow_analysis.Graph.Node;
import traducao.Temp.Label;
import traducao.Temp.LabelList;
import traducao.Temp.TempList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.Targets;

public class AssemFlowGraph extends FlowGraph {
    private Hashtable<Node, Instr> map;
    private Hashtable<Instr, Node> revMap;
        
    public AssemFlowGraph(InstrList list) {
        super();        
        map = new Hashtable<Node, Instr>();
        revMap = new Hashtable<Instr, Node>();        
        buildGraph(list);
    }
        
    private void buildGraph(InstrList ilist) { 
        Hashtable<Label, Instr> map1 = new Hashtable<Label, Instr>();
        for( InstrList a = ilist ; a != null; a = a.tail ) {
            Node n = this.newNode();            
            map.put(n, a.head);            
            revMap.put(a.head, n);
            
            if ( a.head instanceof LABEL ) {
                map1.put(((LABEL)a.head).label, a.head );
            }
        }
        
        for ( InstrList aux = ilist; aux != null; aux = aux.tail ) {
            Targets jmps = aux.head.jumps(); 
            if ( jmps == null ) {
                if (aux.tail != null)
                    this.addEdge(revMap.get(aux.head), revMap.get(aux.tail.head));
            }
            else {
                for ( LabelList a = jmps.labels; a != null; a = a.tail ) {
                    this.addEdge(revMap.get(aux.head),
                            revMap.get(map1.get(a.head)));
                }
            }
        }        
    }
    
    public Instr instr(Node node) {
        return map.get(node);
    }
    
    public Node node(Instr instr) {
        return revMap.get(instr);
    }

    public TempList def(Node node) {
        Instr i = map.get(node);
        
        if ( i == null )
            return null;
        
        return i.def();
    }

    public TempList use(Node node) {
        Instr i = map.get(node);
        
        if ( i == null )
            return null;
        
        return i.use();
    }

    public boolean isMove(Node node) {
        Instr i = map.get(node);
        
        if ( i == null )
            return false;
        
        return i instanceof MOVE;
    }
}
