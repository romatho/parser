package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Assign extends Expressions{

    private String objectIdentifier;
    private Expressions exp;


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
        String expType = exp.getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);

        if(expType.equals("ERROR"))
            return "ERROR";

        // if objectIdentifier.getType() == expType
        return expType;
    }
}
