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
    public String toString(boolean checkerMode)
    {
        String toDisplay = "While(";
        toDisplay += condition.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += body.toString(checkerMode);
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType()
    {
        String condType = condition.getType();
        String bodyType = body.getType();
        // check if there isn't already an error in the lower-level expressions
        if(condType.equals("ERROR") || bodyType.equals("ERROR"))
            return "ERROR";

        if(!condType.equals("bool"))
        {
            System.out.println("FILENAME:" + condition.displayNode() +
                    "SEMANTIC error: expected bool as type for the condition not " + condType);
            return "ERROR";
        }

        return bodyType;
    }
}
