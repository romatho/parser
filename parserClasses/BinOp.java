package parserClasses;


import check.*;

import java.util.ArrayList;
import java.util.HashMap;

public class BinOp extends Expressions{

    private String op;
    private Expressions firstExp;
    private Expressions secondExp;
    private String type =null;

    public BinOp(int pColumn, int pLine, String pOp, Expressions pFirstExp, Expressions pSecondExp)
    {
        super(pColumn, pLine);
        op = pOp;
        firstExp = pFirstExp;
        secondExp = pSecondExp;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "BinOp(";
        toDisplay += op;
        toDisplay += ", ";
        toDisplay += firstExp.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += secondExp.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c)
    {
        if(type!=null)
            return type;
        String firstType = firstExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);
        String secondType = secondExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c);
        type="ERROR";
        switch(op) {
            case "+":
            if(firstType.equals("int32")&&secondType.equals("int32"))
                type="int32";
            else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator + not "+firstType);
            }
            else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator + not "+secondType);
            }
            break;
            case "<":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator < not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator < not "+secondType);
                }
                break;
            case "<=":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator <= not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator <= not "+secondType);
                }
                break;
            case "-":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="int32";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator - not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator - not "+secondType);
                }
                break;
            case "*":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="int32";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator * not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator * not "+secondType);
                }
                break;
            case "/":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="int32";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator / not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator / not "+secondType);
                }
                break;
            case "^":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="int32";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected int32 with operator  not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected int32 with operator ^ not "+secondType);
                }
                break;
            case "and":
                if(firstType.equals("bool")&&secondType.equals("bool"))
                    type="bool";
                else if (!firstType.equals("bool")&&!firstType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println(filename +":"+ firstExp.displayNode()+"semantic error: expected bool with operator and not "+firstType);
            }
            else if (!secondType.equals("bool")&&!secondType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println(filename +":"+ secondExp.displayNode()+"semantic error: expected bool with operator and not "+secondType);
            }
            break;
            case "=":
                if(firstType.equals("ERROR") || secondType.equals("ERROR"))
                {
                    type = "ERROR";
                }
                else if(!firstType.equals(secondType))
                {
                    type = "ERROR";
                    System.err.println(filename +":"+ this.displayNode()+"semantic error: expected same type for both expressions with operator =");
                }
                else
                {
                    type = "bool";
                }
        }
        if(type.equals("ERROR"))
            c.toReturn = 1;
        return type;
    }
}

