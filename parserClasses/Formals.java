package parserClasses;

public class Formals extends Node {
    private String indentifier;
    private Types type;


    public Formals(int pColumn, int pLine, String indentifier, Types type) {
        super(pColumn, pLine);
        this.indentifier = indentifier;
        this.type = type;
    }


    @Override
    public String toString() {
        return "Formal(" + indentifier + ":" + type.toString() + ")";
    }

}