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
                           HashMap<String,String> localVariables, String classe, String methode)
    {

        if(type.getType().equals("ERROR"))
            return "ERROR";

        // if init exists, check if its type corresponds to the one defined for the object
        if(init != null)
        {
            String initType = init.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, methode);
            if(initType.equals("ERROR"))
                return "ERROR";
            if(!initType.equals(type.getType()))
            {
                System.err.println("FILENAME:" + this.displayNode() +
                        "SEMANTIC error: expected same type for the variable and its initialisation");
                return "ERROR";
            }
        }

        // check if there is an error in the expression of the scope with the defined variable
        localVariables.put(objectIdentifier, type.getType());
        String scopeType = scope.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, methode);
        localVariables.remove(objectIdentifier);
        if(scopeType.equals("ERROR"))
            return "ERROR";

        return type.getType();
    }
}

    
