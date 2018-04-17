package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Call extends Expressions {

    private Expressions objectExp;
    private String methodName;
    private ParserArray<Expressions> listExp;
    private String type;

    public Call(int pColumn, int pLine, Expressions pObjectExp, String pMethodName, ParserArray<Expressions> pListExp)
    {
        super(pColumn, pLine);
        objectExp = pObjectExp;
        methodName = pMethodName;
        listExp = pListExp;
        type = null;
    }


    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "Call(";
        toDisplay += objectExp.toString();
        if(checkerMode)
            toDisplay += " : " + type;
        toDisplay += ", ";
        toDisplay += methodName;
        toDisplay += ", ";
        toDisplay += listExp.toString(checkerMode);
        if(checkerMode)
            toDisplay += " : " + type;
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables)
    {
        String objectType = objectExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables);

        if(objectType.equals("ERROR"))
        {
            type = "ERROR";
            return type;
        }

        if(objectType.equals("bool") || objectType.equals("int32") ||
                objectType.equals("string") || objectType.equals("unit"))
        {
            System.err.println("FILENAME:" + objectExp.displayNode() +
                    "SEMANTIC error: A variable of type " + objectType + " cannot have a method.");
            type = "ERROR";
            return type;
        }

        // check if the arguments given to the function have the same type as the formals
        int i = 0;
        for(Expressions e : listExp)
        {
            if(e.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables).equals("ERROR"))
            {
                type = "ERROR";
                return type;
            }
            String argType = e.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables);
            Pair<String, String> argument = classMethodFormalsType.get(objectType).get(methodName).get(i);
            if(!argument.getValue().equals(argType))
            {
                System.err.println("FILENAME:" + objectExp.displayNode() +
                        "SEMANTIC error: The argument " + argument.getKey() + " must be of type " +
                        argument.getValue() + " and not " + argType);
                type = "ERROR";
                return type;
            }
        }

        return classMethodType.get(objectType).get(methodName);
    }
}