package parserClasses;


import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class New extends Expressions {
    private Types typeIdentifier;



    public New(int pColumn, int pLine, Types pTypeIdentifier)
    {
        super(pColumn, pLine);
        typeIdentifier = pTypeIdentifier;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "New(";
        toDisplay += typeIdentifier.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + typeIdentifier.getType();
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g) {
        g.builder.append("    %").append(g.counter).append(" = call %class.")
                .append(type).append("* @").append(type).append("-new()\n");
        value = "%" + g.counter++;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        if(classFieldType.containsKey(typeIdentifier.getType()))
            return typeIdentifier.getType();

        else
        {
            c.toReturn=1;
            System.err.println(filename +":"+ this.displayNode()+"semantic error: "+ typeIdentifier.getType()+" is undefined" );
            return "ERROR";
        }
    }
}
