package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Terminal extends Expressions{

    private String value;
    private String type;

    public Terminal(int pColumn, int pLine, String pValue,String pType)
    {
        super(pColumn, pLine);
        value = pValue;
        type= pType;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = value;
        if(checkerMode)
            toDisplay += " : " + getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodeType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                           HashMap<String,String> localVariables)
    {
        if(type.equals("bool") || type.equals("int32") ||
                type.equals("string") || type.equals("unit"))
            return type;

        return "";
    }


}
