package parserClasses;

public class Field  extends Node {
    private String identifier;
    private Types type;
    private Expressions expr;


    public Field(int pColumn, int pLine, String identifier, Types type,Expressions expr) {
        super(pColumn, pLine);
        this.identifier = identifier;
        this.type = type;
        this.expr= expr;
    }

    @Override
    public String toString() {
        if(expr==null)
            return "Field(" + identifier + ", " + type.toString() +")";
        else
            return "Field(" + identifier + ", " + type.toString() +", "+ expr.toString()+")";
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type.getType();
    }
}