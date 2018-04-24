package parserClasses;


import check.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        toDisplay += objectExp.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += methodName;
        toDisplay += ", ";
        toDisplay += listExp.toString(checkerMode);
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

        if(type != null)
            return type;

        String objectType = objectExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);

        if(objectType.equals("ERROR"))
        {
            type = "ERROR";
            c.toReturn=1;
            return type;
        }
        // check if the call is really made on a method of a class
        if(objectType.equals("bool") || objectType.equals("int32") ||
                objectType.equals("string") || objectType.equals("unit"))
        {
            System.err.println(filename +":" + objectExp.displayNode() +
                    "SEMANTIC error: A variable of type " + objectType + " cannot have a method.");
            type = "ERROR";
            c.toReturn=1;
            return type;
        }
        // check if the class contains the specified method or not
        if((objectType.equals("SELF") && classMethodType.get(classe).get(methodName) == null) ||
                ((!objectType.equals("SELF") && classMethodType.get(objectType).get(methodName) == null)))
//        if(classMethodType.get(objectType).get(methodName) == null)
        {
            System.err.println(filename +":" + objectExp.displayNode() +
                    "SEMANTIC error: An object of class " + objectType + " doesn't have a method " + methodName);
            c.toReturn=1;
            type = "ERROR";
            return type;
        }

        // check if the arguments given to the function have the same type as the formals
        int i = 0;
        for(Expressions e : listExp)
        {
            if(e.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c).equals("ERROR"))
            {
                c.toReturn=1;
                type = "ERROR";
                return type;
            }
            String argType = e.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);
            Pair argument;
            if(objectType.equals("SELF"))
                argument = classMethodFormalsType.get(classe).get(methodName).get(i);
            else
                argument = classMethodFormalsType.get(objectType).get(methodName).get(i);


            if(!argument.getValue().equals(argType))
            {
                if(!c.childHasParent(argType,argument.getValue())) {
                    System.err.println(filename + ":" + objectExp.displayNode() +
                            "SEMANTIC error: The argument " + argument.getKey() + " must be of type " +
                            argument.getValue() + " and not " + argType);
                    c.toReturn = 1;
                    type = "ERROR";
                    return type;
                }
                type = argument.getValue();
                return type;
            }
            ++i;
        }

        // The method call is valid. Return the type defined for the method.
        type = classMethodType.get(objectType).get(methodName);
        return type;
    }
}