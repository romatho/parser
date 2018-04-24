package parserClasses;

import check.*;

import java.util.ArrayList;
import java.util.HashMap;

public class UnOp extends Expressions{
    private String firstElement;
    private Expressions exp;
    private String type =null;

    public UnOp(int pColumn, int pLine, String pFirstElement, Expressions pExp)
    {
        super(pColumn, pLine);
        firstElement = pFirstElement;
        exp = pExp;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "UnOp(";
        toDisplay += firstElement;
        toDisplay += ", ";
        toDisplay += exp.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c)
    {
        if(type!=null)
            return type;
        String expType = exp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);
        // check if there isn't already an error in the lower-level expressions
        if(expType.equals("ERROR"))
        {
            c.toReturn=1;
            type= "ERROR";
        }

        switch(firstElement)
        {
            case "not":
                if(!expType.equals("bool"))
                {
                    System.out.println(filename +":" + exp.displayNode() +
                            "SEMANTIC error: expected bool with operator 'not' not " + expType);
                    type= "ERROR";
                    c.toReturn=1;
                }
                type= "bool";
                return type;
            case "-":
                if(!expType.equals("int32"))
                {
                    System.out.println(filename +":" + exp.displayNode() +
                            "SEMANTIC error: expected int32 with operator '-' not " + expType);
                    c.toReturn=1;
                    type= "ERROR";
                }
                type= "int32";
                return type;
            case "isnull":
                type= "bool";
                return type;
        }
        c.toReturn=1;
        type= "ERROR";
        return type;
    }
}
