package parserClasses;

public class BinOp extends Expressions{

    private String op;
    private Expressions firstExp;
    private Expressions secondExp;

    public BinOp(int pColumn, int pLine, String pOp, Expressions pFirstExp, Expressions pSecondExp)
    {
        super(pColumn, pLine);
        op = pOp;
        firstExp = pFirstExp;
        secondExp = pSecondExp;
    }

    @Override
    public String toString()
    {
        String toDisplay = "BinOp(";
        toDisplay += op;
        toDisplay += ", ";
        toDisplay += firstExp.toString();
        toDisplay += ", ";
        toDisplay += secondExp.toString();
        toDisplay += ")";
        return toDisplay;
    }
}

