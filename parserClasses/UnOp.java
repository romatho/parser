package parserClasses;

import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class UnOp extends Expressions{
    private String firstElement;
    private Expressions exp;
    public String type =null;

    public UnOp(int pColumn, int pLine, String pFirstElement, Expressions pExp)
    {
        super(pColumn, pLine);
        firstElement = pFirstElement;
        exp = pExp;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "UnOp(";
        toDisplay += firstElement;
        toDisplay += ", ";
        toDisplay += exp.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g) {
        StringBuilder builder=new StringBuilder();
        this.exp.toLlvm(g);
        String exp=this.exp.value;
        switch(firstElement)
        {
            case "not":
                if (exp.equals("true"))
                    this.value="false";
                else if (exp.equals("false"))
                    this.value="true";
                else {
                    // Otherwise compute it
                    g.builder.append("    " + "%").append( g.counter).append(" = xor i1 ").append(exp).append(", true\n");
                    this.value="%" + g.counter++;
                }
                break;
            case "-":
                if (exp.contains("%")) {
                    g.builder.append("    " + "%").append(g.counter).append(" = sub i32 0, ").append(exp).append("\n");
                   this.value="%" + g.counter++;
                }
                // It is a int so just change the sign
                else if (exp.contains("-"))
                    this.value=exp.replaceFirst("-", "");
                else
                    this.value="-" + exp;
                break;
            case "isnull":
                if (exp.equals("null"))
                    // Directly give the value
                    this.value="true";
                else {
                    // Otherwise compute it
                    g.builder.append("    " + "%").append(g.counter).append(" = icmp eq ").append(g.typeConversion(this.exp.type.replace(" : ", ""))).append(" ").append(exp).append(", null\n");
                    this.value="%" + g.counter++;
                }
        }

    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        if(type!=null)
            return type;
        String expType = exp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
        // check if there isn't already an error in the lower-level expressions
        if(expType.equals("ERROR"))
        {
            c.toReturn=1;
            type= "ERROR";
        }

        switch(firstElement)
        {
            case "not":
                if(!expType.equals("bool"))
                {
                    System.err.println(filename +":" + exp.displayNode() +
                            "semantic error: expected bool with operator 'not' not " + expType);
                    type= "ERROR";
                    c.toReturn=1;
                }
                type= "bool";
                return type;
            case "-":
                if(!expType.equals("int32"))
                {
                    System.err.println(filename +":" + exp.displayNode() +
                            "semantic error: expected int32 with operator '-' not " + expType);
                    c.toReturn=1;
                    type= "ERROR";
                }
                type= "int32";
                return type;
            case "isnull":
                if(expType.equals("bool") || expType.equals("int32") ||
                        expType.equals("string") || expType.equals("unit"))
                {
                    type = "ERROR";
                    c.toReturn = 1;
                    System.err.println(filename +":" + exp.displayNode() +
                            "semantic error: this literal has type " + expType + ", but expected type was Object");
                    return type;
                }
                type= "bool";
                return type;
        }
        c.toReturn=1;
        type= "ERROR";
        return type;
    }
}
