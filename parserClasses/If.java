package parserClasses;

public class If extends Expression {
    private Expression condition;
    private Expression thenStatement;
    private Expression elseStatement;



    public If(int pColumn, int pLine, Expression pCondition, Expression pThen)
    {
        super(pColumn, pLine);
        condition = pCondition;
        thenStatement = pThen;
        elseStatement = null;
    }

    public If(int pColumn, int pLine, Expression pCondition, Expression pThen, Expression pElse)
    {
        super(pColumn, pLine);
        condition = pCondition;
        thenStatement = pThen;
        elseStatement = pElse;
    }

    @Override
    public String toString()
    {
        String toDisplay = "If(";
        toDisplay += condition.toString();
        toDisplay += ", ";
        toDisplay += thenStatement.toString();

        if(elseStatement != null)
        {
            toDisplay += ", ";
            toDisplay += elseStatement.toString();
        }

        toDisplay += ")";
        return toDisplay;
    }


}
