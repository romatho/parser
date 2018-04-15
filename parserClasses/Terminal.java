package parserClasses;

public class Terminal extends Expressions{

    private String value;
    private String type;

    public Terminal(int pColumn, int pLine, String pValue,String pType)
    {
        super(pColumn, pLine);
        value = pValue;
        type= pType;
    }

    @Override
    public String toString()
    {
        String toDisplay = value;
        return toDisplay;
    }
    @Override
    public String getType()
    {
        String toReturn = type;
        return toReturn;
    }


}
