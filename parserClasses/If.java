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

    @Override
    public String getType()
    {
        if(!condition.getType().equals("bool")&&!condition.getType().equals("ERROR"))
            return "ERROR";
        if(condition.getType().equals("ERROR"))
            return "ERROR";
        if(thenStatement.getType().equals(elseStatement.getType())&&!thenStatement.getType().equals("ERROR"))
            return thenStatement.getType();
        else if (!thenStatement.getType().equals("ERROR")&&!elseStatement.getType().equals("ERROR"))
        {
            System.out.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            return "ERROR";
        }
        else
            return "ERROR";

    }
}
