package parserClasses;

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
    public String getType()
    {
        return "";
    }
}
