package parserClasses;

import javafx.util.Pair;
import check.Checker;

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
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c)
    {
        if(condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("ERROR") || thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("ERROR") ||
                elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("ERROR"))
            {
            c.toReturn=1;
            return "ERROR";

        }

        if(!condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("bool"))
        {
            System.err.println(filename +":"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            c.toReturn=1;
            return "ERROR";
        }
        if(elseStatement==null)
            return thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);
        if(thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals(elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c)))
            return thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);
        else if (thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("unit"))
        {
            return "unit";
        }
        else if (elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("unit"))
        {
            return "unit";
        }
        else
        {
            c.toReturn=1;
            System.err.println(filename +":"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            return "ERROR";
        }

    }
}
