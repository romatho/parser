package parserClasses;

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
    public String toString()
    {
        String toDisplay = "Assign(";
        toDisplay += objectIdentifier;
        toDisplay += ", ";
        toDisplay += exp.toString();
        toDisplay += ")";
        return toDisplay;
    }
}
