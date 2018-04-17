package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Assign extends Expressions{

    private String objectIdentifier;
    private Expressions exp;
    private String type = null;


    public Assign(int pColumn, int pLine, String pObjectIdentifier, Expressions pExp)
    {
        super(pColumn, pLine);
        objectIdentifier = pObjectIdentifier;
        exp = pExp;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "Assign(";
        toDisplay += objectIdentifier;
        toDisplay += ", ";
        toDisplay += exp.toString();
        if(checkerMode)
            toDisplay += " : " + type;
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                          HashMap<String, HashMap<String, String> > classMethodType,
                          HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType, HashMap<String,String> localVariables, String classe)
    {
        if(type != null)
            return type;

        type = exp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe);
        if(type.equals("ERROR"))
            return type;
        String fieldType = classFieldType.get();
        String localType = localVariables.get(objectIdentifier);
        if(localType != null && !localType.equals(type))
        {
            System.err.println("FILENAME:" + this.displayNode() +
                    "SEMANTIC error: expected the assigned expression's type to be equal to the variable's type");
            type = "ERROR";
        }

        // if objectIdentifier.getType() == expType
        return type;
    }
}
