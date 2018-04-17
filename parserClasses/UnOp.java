package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class UnOp extends Expressions{
    private String firstElement;
    private Expressions exp;

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
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodeType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                           HashMap<String,String> localVariables)
    {
        String expType = exp.getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);
        // check if there isn't already an error in the lower-level expressions
        if(expType.equals("ERROR"))
            return "ERROR";

        switch(firstElement)
        {
            case "not":
                if(!expType.equals("bool"))
                {
                    System.out.println("FILENAME:" + exp.displayNode() +
                            "SEMANTIC error: expected bool with operator 'not' not " + expType);
                    return "ERROR";
                }
                return "bool";

            case "-":
                if(!expType.equals("int32"))
                {
                    System.out.println("FILENAME:" + exp.displayNode() +
                            "SEMANTIC error: expected bool with operator '-' not " + expType);
                    return "ERROR";
                }
                return "int32";

            case "isnull":
                return "bool";
        }
        return "ERROR";
    }
}
