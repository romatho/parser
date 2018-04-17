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
    public String toString(boolean checkerMode)
    {
        String toDisplay = value;
        if(checkerMode)
            toDisplay += " : " + getType();
        return toDisplay;
    }

    @Override
    public String getType()
    {
        if(type.equals("bool") || type.equals("int32") ||
                type.equals("string") || type.equals("unit"))
            return type;


    }


}
