import Assem.InstrList;
import analisador_semantico.syntaxtree.*;
import analisador_semantico.visitor.*;
import traducao.Mips.MipsFrame;
import traducao.visitor.ImplementationIR;
import traducao.Tree.Print;
import traducao.Tree.StmList;
import traducao.Canon.BasicBlocks;
import traducao.Canon.Canon;
import traducao.Canon.TraceSchedule;
import traducao.FragAux.*;
import dataflow_analysis.FlowGraph.*;

public class Main {
   public static void main(String [] args) throws Exception{
      MiniJavaParser parser = new MiniJavaParser(new java.io.FileInputStream(new java.io.File(args[0])));
      Program root = parser.Goal();
      DepthFirstVisitor program = new DepthFirstVisitor();
      root.accept(program);
      System.out.println("A Tabela de Símbolos foi gerada com sucesso!");
      root.accept(new TypeDepthFirstVisitor(program.program_table()));
      System.out.println("Os tipos do programa estão corretos.");
      MipsFrame frame = new MipsFrame();
      ImplementationIR translate_ir = new ImplementationIR(frame, program.program_table());
      root.accept(translate_ir);

      Print printer = new Print(System.out);
      Frag next_frag = translate_ir.frag_head.getNext();
      TraceSchedule trace_schedule;
      
      //Print apenas do primeiro frag
      trace_schedule = new TraceSchedule(new BasicBlocks(Canon.linearize(((ProcFrag)next_frag).body)));

      StmList stm_list = trace_schedule.stms;
      while (!(stm_list.tail == null)) {
         printer.prStm(stm_list.head);
         stm_list = stm_list.tail;
      }

      //((ProcFrag)next_frag).frame.codegen(trace_schedule.stms);

      InstrList instrlist = frame.codegen(trace_schedule.stms);
      AssemFlowGraph assemflowgraph = new AssemFlowGraph(instrlist);
      assemflowgraph.show(System.out);
      
      //Print em todo o código intermediario
      /*
      while(!(next_frag.getNext() == null)){
         next_frag = next_frag.getNext();
         trace_schedule = new TraceSchedule(new BasicBlocks(Canon.linearize(((ProcFrag)next_frag).body)));

         while (!(trace_schedule.stms.tail == null)) {
            printer.prStm(trace_schedule.stms.head);
            trace_schedule.stms = trace_schedule.stms.tail;
         }

         ((ProcFrag)next_frag).frame.codegen(trace_schedule.stms);
         
      } */
   }
}
