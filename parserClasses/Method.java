package parserClasses;

public class Method extends Node {

    private ParserArray<Expressions> formals;
    private String Identifier;
    private Types ReturnType;
    private Blocks block;


    public Method(int pColumn, int pLine, String Identifier, ParserArray<Expressions> formals,
                  Types ReturnType, Blocks expr)
    {
        super(pColumn, pLine);
        this.Identifier = Identifier;
        this.formals = formals;
        this.ReturnType = ReturnType;
        this.block = expr;
    }


    @Override
    public String toString() {
        return "Method(" + Identifier + ", " + formals.toString()
                + ", " + ReturnType.toString() + ", " + block.toString() + ")";
    }
}