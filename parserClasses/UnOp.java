package parserClasses;

public class UnOp extends Expressions{
    private String firstElement;
    private Expressions exp;

    public UnOp(int pColumn, int pLine, String pFirstElement, Expressions pExp)
    {
        super(pColumn, pLine);
        firstElement = pFirstElement;
        exp = pExp;
    }

    @Override
    public String toString()
    {
        String toDisplay = "UnOp(";
        toDisplay += firstElement;
        toDisplay += ", ";
        toDisplay += exp.toString();
        toDisplay += ")";
        return toDisplay;
    }
}
