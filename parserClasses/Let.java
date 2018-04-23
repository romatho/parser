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
            toDisplay += init.toString(false);
            toDisplay += " : " + type.toString(checkerMode);
        }
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + type.toString(checkerMode);
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode)
    {

        if(type.getType().equals("ERROR"))
            return "ERROR";

        // if init exists, check if its type corresponds to the one defined for the object
        if(init != null)
        {
            String initType = init.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode);
            if(initType.equals("ERROR"))
                return "ERROR";
            if(!initType.equals(type.getType()))
            {
                System.err.println(filename +":" + this.displayNode() +
                        "SEMANTIC error: expected same type for the variable and its initialisation");
                return "ERROR";
            }
        }

        // check if there is an error in the expression of the scope with the defined variable
        String ltype="OI";
        if (localVariables.get(objectIdentifier) == null) {
                if (classFieldType.get(classe).get(objectIdentifier) == null) {
                    ArrayList<Pair<String, String>> temp = classMethodFormalsType.get(classe).get(methode);
                    int i =0;
                    for ( i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getKey().equals(objectIdentifier)) {
                            ltype = temp.get(i).getValue();
                        }
                        }
                        if(i==temp.size())
                        {
                            System.err.println(filename +":" + this.displayNode() +
                        "SEMANTIC error: Unknown variable " + objectIdentifier);
                        return "ERROR";
                        }
                }
                else
                {
                    ltype = classFieldType.get(classe).get(objectIdentifier);
                }
            } else {
                ltype = localVariables.get(objectIdentifier);

            }
        localVariables.put(objectIdentifier,ltype);
        String scopeType = scope.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode);
        localVariables.remove(objectIdentifier);
        if(scopeType.equals("ERROR"))
            return "ERROR";

        return ltype;
    }
}

    
