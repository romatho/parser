package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Let extends Expressions{

    private String objectIdentifier;
    private Types type;
    private Expressions scope;
    private Expressions init;


    public Let(int pColumn, int pLine, String pObjectIdentifier, Types pType, Expressions pScope)
    {
        super(pColumn, pLine);
        objectIdentifier = pObjectIdentifier;
        type = pType;
        scope = pScope;
        init = null;
    }

    public Let(int pColumn, int pLine, String pObjectIdentifier, Types pType, Expressions pScope, Expressions pInit)
    {
        super(pColumn, pLine);
        objectIdentifier = pObjectIdentifier;
        type = pType;
        scope = pScope;
        init = pInit;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "Let(";
        toDisplay += objectIdentifier;
        toDisplay += ", ";
        toDisplay += type.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += scope.toString(checkerMode);
        if(init != null)
        {
            toDisplay += ", ";
            toDisplay += init.toString(checkerMode);
        }
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables)
    {
        return "";
    }
}

    
