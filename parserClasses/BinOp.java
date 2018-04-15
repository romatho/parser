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
    @Override
    public String getType()
    {
        String firstType = firstExp.getType();
        String secondType = secondExp.getType();
        String toReturn="ERROR";
        switch(op) {
            case "+":
            if(firstType.equals("int32")&&secondType.equals("int32"))
                toReturn="int32";
            else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
            {
                toReturn="ERROR";
                System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator + not "+firstType);
            }
            else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
            {
                toReturn="ERROR";
                System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator + not "+secondType);
            }
            break;
            case "<":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    toReturn="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator < not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator < not "+secondType);
                }
                break;
            case "<=":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    toReturn="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator <= not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator <= not "+secondType);
                }
                break;
            case "-":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    toReturn="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator - not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator - not "+secondType);
                }
                break;
            case "*":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    toReturn="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator * not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator * not "+secondType);
                }
                break;
            case "/":
                if(firstType.equals("INTEGER_LITERAL")&&secondType.equals("INTEGER_LITERAL"))
                    toReturn="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator / not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator / not "+secondType);
                }
                break;
            case "^":
                if(firstType.equals("INTEGER_LITERAL")&&secondType.equals("INTEGER_LITERAL"))
                    toReturn="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator  not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    toReturn="ERROR";
                    System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator ^ not "+secondType);
                }
                break;
            case "and":
                if(firstType.equals("bool")&&secondType.equals("bool"))
                    toReturn="bool";
                else if (!firstType.equals("bool")&&!firstType.equals("ERROR"))
            {
                toReturn="ERROR";
                System.out.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected bool with operator and not "+firstType);
            }
            else if (!secondType.equals("bool")&&!secondType.equals("ERROR"))
            {
                toReturn="ERROR";
                System.out.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected bool with operator and not "+secondType);
            }
            break;
            case "_":
                if(firstType.equals(secondType)&&!secondType.equals("ERROR"))
                    toReturn="bool";
                else if (!secondType.equals("ERROR")&&!firstType.equals("ERROR"))
            {
                toReturn="ERROR";
                System.out.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            }
        }
        return toReturn;
    }
}

