package parserClasses;

public class While extends Expressions {

    private Expressions condition;
    private Expressions body;



    public While(int pColumn, int pLine, Expressions pCondition, Expressions pBody)
    {
        super(pColumn, pLine);
        condition = pCondition;
        body = pBody;
    }

    @Override
    public String toString()
    {
        String toDisplay = "While(";
        toDisplay += condition.toString();
        toDisplay += ", ";
        toDisplay += body.toString();
        toDisplay += ")";
        return toDisplay;
    }
}
