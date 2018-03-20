package parserClasses;

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
    public String toString()
    {
        String toDisplay = "Let(";
        toDisplay += objectIdentifier;
        toDisplay += ", ";
        toDisplay += type.toString();
        toDisplay += ", ";
        toDisplay += scope.toString();
        if(init != null)
        {
            toDisplay += ", ";
            toDisplay += init.toString();
        }
        toDisplay += ")";
        return toDisplay;
    }
}

    
