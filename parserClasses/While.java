package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class While extends Expressions {

    private Expressions condition;
    private Expressions body;
    private String type =null;



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
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables)
    {
        if(type!=null)
            return  type;
        String condType = condition.getType( classFieldType, classMethodType, classMethodFormalsType, localVariables);
        String bodyType = body.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables);
        // check if there isn't already an error in the lower-level expressions
        if(condType.equals("ERROR") || bodyType.equals("ERROR"))
            type= "ERROR";

        if(!condType.equals("bool"))
        {
            System.out.println("FILENAME:" + condition.displayNode() +
                    "SEMANTIC error: expected bool as type for the condition not " + condType);
            type= "ERROR";
        }

        type= bodyType;
        return type;
    }
}
