package parserClasses;

import java.util.Map;
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
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Map.Entry<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String methode)
    {
        if(classFieldType.containsKey(typeIdentifier.getType()))
            return typeIdentifier.getType();
        else
        {
            System.err.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: "+ typeIdentifier.getType()+" is undefined" );
            return "ERROR";
        }
    }
}
