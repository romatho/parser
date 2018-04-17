package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class New extends Expressions {
    private Types typeIdentifier;



    public New(int pColumn, int pLine, Types pTypeIdentifier)
    {
        super(pColumn, pLine);
        typeIdentifier = pTypeIdentifier;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "New(";
        toDisplay += typeIdentifier.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + typeIdentifier.toString(checkerMode);
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodeType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                           HashMap<String,String> localVariables)
    {
        return "";
    }
}
