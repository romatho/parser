package parserClasses;

import java.util.Map;
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
        toDisplay += firstExp.toString();
        if(checkerMode)
            toDisplay += " : " + type;
        toDisplay += ", ";
        toDisplay += secondExp.toString();
        if(checkerMode)
            toDisplay += " : " + type;
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Map.Entry<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String methode)
    {
        if(type!=null)
            return type;
        String firstType = firstExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, methode);
        String secondType = secondExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, methode);
        type="ERROR";
        switch(op) {
            case "+":
            if(firstType.equals("int32")&&secondType.equals("int32"))
                type="int32";
            else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator + not "+firstType);
            }
            else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator + not "+secondType);
            }
            break;
            case "<":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator < not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator < not "+secondType);
                }
                break;
            case "<=":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator <= not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator <= not "+secondType);
                }
                break;
            case "-":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator - not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator - not "+secondType);
                }
                break;
            case "*":
                if(firstType.equals("int32")&&secondType.equals("int32"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator * not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator * not "+secondType);
                }
                break;
            case "/":
                if(firstType.equals("INTEGER_LITERAL")&&secondType.equals("INTEGER_LITERAL"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator / not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator / not "+secondType);
                }
                break;
            case "^":
                if(firstType.equals("INTEGER_LITERAL")&&secondType.equals("INTEGER_LITERAL"))
                    type="bool";
                else if (!firstType.equals("int32")&&!firstType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected int32 with operator  not "+firstType);
                }
                else if (!secondType.equals("int32")&&!secondType.equals("ERROR"))
                {
                    type="ERROR";
                    System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected int32 with operator ^ not "+secondType);
                }
                break;
            case "and":
                if(firstType.equals("bool")&&secondType.equals("bool"))
                    type="bool";
                else if (!firstType.equals("bool")&&!firstType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println("FILENAME:"+ firstExp.displayNode()+"SEMANTIC error: expected bool with operator and not "+firstType);
            }
            else if (!secondType.equals("bool")&&!secondType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println("FILENAME:"+ secondExp.displayNode()+"SEMANTIC error: expected bool with operator and not "+secondType);
            }
            break;
            case "_":
                if(firstType.equals(secondType)&&!secondType.equals("ERROR"))
                    type="bool";
                else if (!secondType.equals("ERROR")&&!firstType.equals("ERROR"))
            {
                type="ERROR";
                System.err.println("FILENAME:"+ this.displayNode()+"SEMANTIC error: expected same type for both expressions with operator _");
            }
        }
        return type;
    }
}

