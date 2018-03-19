package parserClasses;

public class Terminal extends Expressions{

    private String value;

    public Terminal(int pColumn, int pLine, String pValue)
    {
        super(pColumn, pLine);
        value = pValue;
    }

    @Override
    public String toString()
    {
        String toDisplay = value;
        return toDisplay;
    }

}
