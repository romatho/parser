package parserClasses;

public class While extends Expression {

    private Expression condition;
    private Expression body;



    public If(int pColumn, int pLine, Expression pCondition, Expression pBody)
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
