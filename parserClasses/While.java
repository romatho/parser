package parserClasses;

import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class While extends Expressions {

    private Expressions condition;
    private Expressions body;
    public String type = null;



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
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g)
    {
        ++g.whileCounter;

        g.builder.append("    ").append("br label %while").append(g.whileCounter).append("\n");
        g.builder.append("while").append(g.whileCounter).append(":").append("\n");
        //currentLabel = "%while" + g.whileCounter;
        this.condition.toLlvm(g);



        if (this.condition.value.equals("true")) {
            this.body.toLlvm(g);
            g.builder.append("    ").append("br label %while")
                    .append(g.whileCounter).append("\n");
        } else {
            g.builder.append("    ").append("br i1 ").append(condition.value)
                    .append(", label %while").append(g.whileCounter)
                    .append(", label %end_while").append(g.whileCounter)
                    .append("\n");
        }


        //currentLabel = "%end_while" + g.whileCounter;
        g.builder.append("end_while").append(g.whileCounter).append(":").append("\n");
    }
    

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair > > > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        if(type!=null)
            return  type;
        String condType = condition.getType( classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode,c, fieldExpr);
        String bodyType = body.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode,c, fieldExpr);
        // check if there isn't already an error in the lower-level expressions
        if(condType.equals("ERROR") )
        {
            c.toReturn=1;
            type= "ERROR";
        }

        if(!condType.equals("bool"))
        {
            System.err.println(filename +":" + condition.displayNode() +
                    " semantic error: expected bool as type for the condition not " + condType);
            type= "ERROR";
            c.toReturn=1;
        }

        type= "unit";
        return type;
    }
}
