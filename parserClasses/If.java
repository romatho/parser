package parserClasses;

public class If extends Expressions {
    private Expressions condition;
    private Expressions thenStatement;
    private Expressions elseStatement;



    public If(int pColumn, int pLine, Expressions pCondition, Expressions pThen)
    {
        super(pColumn, pLine);
        condition = pCondition;
        thenStatement = pThen;
        elseStatement = null;
    }

    public If(int pColumn, int pLine, Expressions pCondition, Expressions pThen, Expressions pElse)
    {
        super(pColumn, pLine);
        condition = pCondition;
        thenStatement = pThen;
        elseStatement = pElse;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "If(";
        toDisplay += condition.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += thenStatement.toString(checkerMode);

        if(elseStatement != null)
        {
            toDisplay += ", ";
            toDisplay += elseStatement.toString(checkerMode);
        }

        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType()
    {
        if(condition.getType().equals("ERROR") || thenStatement.getType().equals("ERROR") ||
                elseStatement.getType().equals("ERROR"))
            return "ERROR";

        if(!condition.getType().equals("bool"))
        {
            System.err.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            return "ERROR";
        }

        if(thenStatement.getType().equals(elseStatement.getType()))
            return thenStatement.getType();
        else if (thenStatement.getType().equals("unit"))
        {
            return "unit";
        }
        else if (elseStatement.getType().equals("unit"))
        {
            return "unit";
        }
        else
        {
            System.err.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            return "ERROR";
        }

    }
}
