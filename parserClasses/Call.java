package parserClasses;

public class Call extends Expressions {

    private Expressions objectExp;
    private String methodName;
    private ParserArray<Formals> listExp;

    public Call(int pColumn, int pLine, Expressions pObjectExp, String pMethodName, ParserArray<Formals> pListExp)
    {
        super(pColumn, pLine);
        objectExp = pObjectExp;
        methodName = pMethodName;
        listExp = pListExp;
    }


    @Override
    public String toString()
    {
        String toDisplay = "Call(";
        toDisplay += objectExp.toString();
        toDisplay += ", ";
        toDisplay += methodName;
        toDisplay += ", ";
        toDisplay += listExp.toString();
        toDisplay += ")";
        return toDisplay;
    }
}