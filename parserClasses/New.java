package parserClasses;

import javafx.util.Pair;
import check.Checker;

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
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c)
    {
        if(classFieldType.containsKey(typeIdentifier.getType()))
        {
            return typeIdentifier.getType();
        }
        else
        {
            c.toReturn=1;
            System.err.println(filename +":"+ this.displayNode()+"SEMANTIC error: "+ typeIdentifier.getType()+" is undefined" );
            return "ERROR";
        }
    }
}
