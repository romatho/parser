package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Call extends Expressions {

    private Expressions objectExp;
    private String methodName;
    private ParserArray<Expressions> listExp;

    public Call(int pColumn, int pLine, Expressions pObjectExp, String pMethodName, ParserArray<Expressions> pListExp)
    {
        super(pColumn, pLine);
        objectExp = pObjectExp;
        methodName = pMethodName;
        listExp = pListExp;
    }


    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "Call(";
        toDisplay += objectExp.toString();
        if(checkerMode)
            toDisplay += " : " + objectExp.getType();
        toDisplay += ", ";
        toDisplay += methodName;
        toDisplay += ", ";
        toDisplay += listExp.toString(checkerMode);
        if(checkerMode)
            toDisplay += " : " + getType();
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodeType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                           HashMap<String,String> localVariables)
    {
        String objectType = objectExp.getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);

        if(objectType.equals("ERROR"))
            return "ERROR";

        if(objectType.equals("bool") || objectType.equals("int32") ||
                objectType.equals("string") || objectType.equals("unit"))
        {
            System.err.println("FILENAME:" + objectExp.displayNode() +
                    "SEMANTIC error: a variable of type " + objectType + " cannot have a method");
            return "ERROR";
        }



        String[] listType = new String[listExp.size()];

        int i = 0;
        for(Expressions e : listExp)
        {
            if(e.getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables).equals("ERROR"))
                return "ERROR";
            listType[i++] = e.getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);
        }

        //TODO: return smth else
        return "";
    }
}