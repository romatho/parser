package parserClasses;

public class Method extends Node {

    private ParserArray<Expressions> formals;
    private String objectId;
    private Types ReturnType;
    private ParserArray<Expressions> block;


    public Method(int pColumn, int pLine, String objectId, ParserArray<Expressions> formals,
                  Types ReturnType, ParserArray<Expressions> expr) {
        super(pColumn, pLine);
        this.objectId = objectId;
        this.formals = formals;
        this.ReturnType = ReturnType;
        this.block = expr;
    }

}