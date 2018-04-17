package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Assign extends Expressions{

    private String objectIdentifier;
    private Expressions exp;
    private String type =null;


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
                          HashMap<String, HashMap<String, String> > classMethodeType,
                          HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType, HashMap<String,String> localVariables)
    {
        if(type!=null)
            return  type;
        type = exp.getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);


        // if objectIdentifier.getType() == expType
        return type;
    }
}
