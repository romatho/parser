package parserClasses;

public class New extends Expressions {
    private Types typeIdentifier;



    public New(int pColumn, int pLine, Types pTypeIdentifier)
    {
        super(pColumn, pLine);
        typeIdentifier = pTypeIdentifier;
    }

    @Override
    public String toString()
    {
        String toDisplay = "New(";
        toDisplay += typeIdentifier.toString();
        toDisplay += ")";
        return toDisplay;
    }
}
