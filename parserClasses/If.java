package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class If extends Expressions {
    private Expressions condition;
    private Expressions thenStatement;
    private Expressions elseStatement;



    public If(int pColumn, int pLine, Expressions pCondition, Expressions pThen)
    {
        super(pColumn, pLine);
        condition = pCondition;
        thenStatement = pThen;
        elseStatement = null;
    }

    public If(int pColumn, int pLine, Expressions pCondition, Expressions pThen, Expressions pElse)
    {
        super(pColumn, pLine);
        condition = pCondition;
        thenStatement = pThen;
        elseStatement = pElse;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "If(";
        toDisplay += condition.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += thenStatement.toString(checkerMode);

        if(elseStatement != null)
        {
            toDisplay += ", ";
            toDisplay += elseStatement.toString(checkerMode);
        }

        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables)
    {
        if(condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("ERROR") || thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("ERROR") ||
                elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("ERROR"))
            return "ERROR";

        if(!condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("bool"))
        {
            System.err.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            return "ERROR";
        }

        if(thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals(elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables)))
            return thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables);
        else if (thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("unit"))
        {
            return "unit";
        }
        else if (elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("unit"))
        {
            return "unit";
        }
        else
        {
            System.err.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            return "ERROR";
        }

    }
}
