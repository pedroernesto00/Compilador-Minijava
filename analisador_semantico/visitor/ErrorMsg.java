package analisador_semantico.visitor;

public class ErrorMsg {
    boolean anyErrors;
    
    void complain(String msg) {
        anyErrors = true;
        System.out.println(msg);
    }
}