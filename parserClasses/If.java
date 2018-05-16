package parserClasses;

import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class If extends Expressions {
    private Expressions condition;
    private Expressions thenStatement;
    private Expressions elseStatement;
    public String type = null;


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
    public void toLlvm(Generator g) {
        return null;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        if(type != null)
            return type;


        if(!condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("bool"))
        {
            System.err.println(filename +":"+ this.displayNode()+"semantic error: expected same type for both expressions with operator _");
            c.toReturn=1;
            type = "ERROR";
            return "ERROR";
        }
        if(elseStatement==null)
        {
            type="unit";
            return type;
        }
        if(condition.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("ERROR") || thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("ERROR") ||
                elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("ERROR"))
            {
            c.toReturn=1;
            type = "ERROR";
            return "ERROR";
        }



        if(c.childHasParent(thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr),elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr))) {
            type = elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
            return type;
        }
        if(c.childHasParent(elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr),thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr))) {
            type = thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
            return type;
        }
        if(thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals(elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr)))
        {
            type = thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
            return type;
        }
        else if (thenStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("unit"))
        {
            type = "unit";
            return "unit";
        }
        else if (elseStatement.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("unit"))
        {
            type = "unit";
            return "unit";
        }
        else
        {
            c.toReturn=1;
            System.err.println(filename +":"+ this.displayNode()+"semantic error: expected same type for both expressions with operator _");
            type = "ERROR";
            return "ERROR";
        }

    }
}
