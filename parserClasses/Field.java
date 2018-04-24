package parserClasses;

import check.Checker;
import check.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Field  extends Expressions {
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
    public String toString(boolean checkerMode) {
        if(expr==null)
            return "Field(" + identifier + ", " + type.toString(checkerMode) +")";
        else
        {
            String toDisplay = "Field(" + identifier + ", " + type.toString(checkerMode) +", "+ expr.toString(checkerMode)+")";
            return toDisplay;
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType()
    {
        return type.getType();
    }

    public String getType(HashMap<String, HashMap<String, String>> classFieldType,
                          HashMap<String, HashMap<String, String> > classMethodType,
                          HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                          HashMap<String,String> localVariables, String classe, String filename,
                          String methode, Checker c, boolean fieldExpr) {
        if(expr == null)
            return type.getType();

        return expr.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode, c, true);
    }

    public Expressions getExpression() {
        return expr;
    }
}