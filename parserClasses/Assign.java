package parserClasses;

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
    public String getType()
    {
        String expType = exp.getType();

        if(expType.equals("ERROR"))
            return "ERROR";

        // if objectIdentifier.getType() == expType
        return expType;
    }
}
