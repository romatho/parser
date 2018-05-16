package parserClasses;

import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class While extends Expressions {

    private Expressions condition;
    private Expressions body;
    public String type = null;



    public While(int pColumn, int pLine, Expressions pCondition, Expressions pBody)
    {
        super(pColumn, pLine);
        condition = pCondition;
        body = pBody;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "While(";
        toDisplay += condition.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += body.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g) {
        return null;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair > > > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        if(type!=null)
            return  type;
        String condType = condition.getType( classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode,c, fieldExpr);
        String bodyType = body.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode,c, fieldExpr);
        // check if there isn't already an error in the lower-level expressions
        if(condType.equals("ERROR") )
        {
            c.toReturn=1;
            type= "ERROR";
        }

        if(!condType.equals("bool"))
        {
            System.err.println(filename +":" + condition.displayNode() +
                    " semantic error: expected bool as type for the condition not " + condType);
            type= "ERROR";
            c.toReturn=1;
        }

        type= "unit";
        return type;
    }
}
