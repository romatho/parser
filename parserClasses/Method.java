package parserClasses;

public class Method extends Node{

    private ParserArray<Formals> formals;
    private String Identifier;
    private Types ReturnType;
    private Blocks block;


    public Method(int pColumn, int pLine, String Identifier, ParserArray<Formals> formals,
                  Types ReturnType, Blocks expr)
    {
        super(pColumn, pLine);
        this.Identifier = Identifier;
        this.formals = formals;
        this.ReturnType = ReturnType;
        this.block = expr;
    }


    @Override
    public String toString(boolean checkerMode) {
        return "Method(" + Identifier + ", " + formals.toString(checkerMode)
                + ", " + ReturnType.toString(checkerMode) + ", " + block.toString(checkerMode) + ")";
    }

    public String getIdentifier() {
        return Identifier;
    }
    public ParserArray<Formals> getFormals() {
        return formals;
    }

    public String getreturnType() {
        return Identifier;
    }

    public String getReturnType() {
        return ReturnType.getType();
    }
}