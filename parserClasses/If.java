package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class If extends Expressions {
    private Expressions condition;
    private Expressions thenStatement;
    private Expressions elseStatement;
    private String type = null;

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
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode)
    {
        System.out.println("getType called");
        if(type != null)
            return type;

        if(condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals("ERROR") || thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals("ERROR") ||
                elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals("ERROR"))
        {
            type = "ERROR";
            return "ERROR";
        }

        if(!condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals("bool"))
        {
            System.err.println(filename +":"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            type = "ERROR";
            return "ERROR";
        }
        if(elseStatement==null)
        {
            type = thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode);
            return type;
        }

        if(thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals(elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode)))
        {
            type = thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode);
            return type;
        }

        else if (thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals("unit"))
        {
            type = "unit";
            return "unit";
        }
        else if (elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode).equals("unit"))
        {
            type = "unit";
            return "unit";
        }
        else
        {
            System.err.println(filename +":"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            type = "ERROR";
            return "ERROR";
        }
    }
}
