package parserClasses;


import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class BinOp extends Expressions{

    private String op;
    private Expressions firstExp;
    private Expressions secondExp;
    public String type =null;

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
    public void toLlvm(Generator g) {
        firstExp.toLlvm(g);
        secondExp.toLlvm(g);
        switch(op) {
            case "+":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                    this.value=String.valueOf(Integer.parseInt(firstExp.value)
                            + Integer.parseInt(secondExp.value));
                else {
                    g.builder.append("    " + "%").append(g.counter).append(" = add i32 ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                    this.value="%" + String.valueOf(g.counter++);
                }
                break;
                
            case "<":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                {
                    if (Integer.parseInt(firstExp.value) < Integer.parseInt(secondExp.value))
                        this.value="true";
                    else
                        this.value="false";
                } else {
                    g.builder.append("    " + "%").append(g.counter).append(" = icmp slt i32 ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                    this.value="%" + String.valueOf(g.counter++);
                }
                break;
                break;
            case "<=":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                {
                    if (Integer.parseInt(firstExp.value) <= Integer.parseInt(secondExp.value))
                        this.value="true";
                    else
                        this.value="false";
                } else {
                    g.builder.append("    " + "%").append(g.counter).append(" = icmp sle i32 ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                    this.value="%" + String.valueOf(g.counter++);
                }
                break;
            case "-":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                   this.value=String.valueOf(Integer.parseInt(firstExp.value)
                            - Integer.parseInt(secondExp.value));
                else {
                    g.builder.append("    " + "%").append(g.counter).append(" = sub i32 ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                  this.value="%" + String.valueOf(g.counter++);
                }
                break;
            case "*":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                    this.value=String.valueOf(Integer.parseInt(firstExp.value)
                            * Integer.parseInt(secondExp.value));
                else {
                    g.builder.append("    " + "%").append(g.counter).append(" = mul i32 ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                   this.value="%" + String.valueOf(g.counter++);
                }
                break;
            case "/":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                    this.value=String.valueOf(Integer.parseInt(firstExp.value)
                            / Integer.parseInt(secondExp.value));
                else {
                    g.builder.append("    " + "%").append(g.counter).append(" = sdiv i32 ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                    this.value="%" + String.valueOf(g.counter++);
                }
                break;
            case "^":
                if (secondExp.value.equals("0"))
                    this.value="1";
                else {
                    
                    g.builder.append("    " + "%").append(g.counter++).append(" = alloca i32\n");
                    g.builder.append("    " + "store i32 1, i32* %").append(g.counter - 1).append("\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = icmp ne i32 ").append(firstExp.value).append(", 1\n");
                    g.builder.append("    " + "br i1 %").append(g.counter - 1).append(", label %if_true").append(g.ifCounter -1).append(", label %end_if").append(g.ifCounter -1).append("\n");
                    g.builder.append("if_true").append(g.ifCounter -1).append(":\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = icmp sge i32 ").append(secondExp.value).append(", 0\n");
                    g.builder.append("    " + "br i1 %").append(g.counter - 1).append(", label %if_true").append(ifCounter).append(", label %if_false").append(ifCounter).append("\n");
                    g.builder.append("if_true").append(g.ifCounter).append(":\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = alloca i32\n");
                    g.builder.append("    " + "store i32 ").append(secondExp.value).append(", i32* %").append(g.counter - 1).append("\n");
                    g.builder.append("    " + "%").append(g.counter++).append("= icmp ne i32 ").append(secondExp.value).append(", 0\n");
                    g.builder.append("    " + "br i1 %").append(g.counter - 1).append(", label %loop").append(whileCounter).append(", label %end_if").append(g.ifCounter -1).append("\n");
                    g.builder.append("loop").append(whileCounter).append(":\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = load i32* %").append(g.counter - 3).append("\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = load i32* %").append(g.counter - 7).append("\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = mul i32 %").append(g.counter - 2).append(", ").append(firstExp.value).append("\n");
                    g.builder.append("    " + "store i32 %").append(g.counter - 1).append(", i32* %").append(g.counter - 8).append("\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = sub i32 %").append(g.counter - 4).append(", 1\n");
                    g.builder.append("    " + "store i32 %").append(g.counter - 1).append(", i32* %").append(g.counter - 6).append("\n");
                    g.builder.append("    " + "%").append(g.counter++).append(" = icmp eq i32 %").append(g.counter - 2).append(", 0\n");
                    g.builder.append("    " + "br i1 %").append(g.counter - 1).append(", label %end_if").append(g.ifCounter -1).append(", label %loop").append(whileCounter).append("\n");
                    g.builder.append("if_false").append(ifCounter).append(":\n");
                    g.builder.append("    " + "store i32 0, i32* %").append(g.counter - 10).append("\n");
                    g.builder.append("    " + "br label %end_if").append(g.ifCounter -1).append("\n");
                    g.builder.append("end_if").append(g.ifCounter -1).append(":\n");
                    g.builder.append("    " + "%").append(g.counter).append(" = load i32* %").append(g.counter - 10).append("\n");
                   this.value="%" + g.counter++;
                    //currentLabel = "%end if" + g.ifCounter -1;
                }
                break;
            case "and":
                if (firstExp.value.equals("false"))
                    this.value="false";
                else if (secondExp.value.equals("true")) {
                    this.value="true";
                } else {
                    
                    g.g.ifCounter++;
                    g.builder.append("    " + "br  i1 ").append(firstExp.value).append(", label %if_true").append(g.g.ifCounter).append(", label %if_false").append(g.g.ifCounter).append("\n");
                    g.builder.append("if_true").append(g.g.ifCounter).append(":\n");
                    currentLabel = "%if_true" + g.g.ifCounter;
                    g.builder.append("    " + "br label %end_if").append(g.g.ifCounter).append("\n");
                    g.builder.append("if_false").append(g.g.ifCounter).append(":\n");
                    g.builder.append("    " + "br label %end_if").append(g.g.ifCounter).append("\n");
                    g.builder.append("end_if").append(g.g.ifCounter).append(":\n");
                    currentLabel = "%end_if" + g.g.ifCounter;
                    g.builder.append("    " + "%").append(g.counter).append(" = phi i1 [").append(secondExp.value).append(", %if_true").append(g.g.ifCounter).append("], [").append(firstExp.value).append(", %if_false").append(g.g.ifCounter).append("]\n");
                    this.value="%" + String.valueOf(g.counter++);
                }
                break;
            case "=":
                if( (firstExp.type.equals("int32")||firstExp.type.equals("unit")||firstExp.type.equals("bool"))&&(secondExp.type.equals("int32")||secondExp.type.equals("unit")||secondExp.type.equals("bool")))
                {
                    if (firstExp.value.equals(secondExp.value))
                       this.value="true";
                    else
                        this.value="false";
                } else {
                    String temp = firstExp.type.replace(" : ", ""), typeR = secondExp.type.replace(" : ", "");

                    if (temp.equals("unit")) {
                        if (typeR.equals("unit"))
                            this.value="true";
                        else
                            this.value="false";
                    } else if (typeR.equals("unit"))
                        this.value="false";
                    else {
                        // If the type is bool or int32 make the comparaison with icmp eq
                        if (temp.equals("int32") || temp.equals("bool")) {
                            g.builder.append("    " + "%").append(g.counter).append(" = icmp eq ").append(g.typeConversion(temp)).append(" ").append(firstExp.value).append(", ").append(secondExp.value).append("\n");
                            this.value="%" + String.valueOf(g.counter++);
                        } else if (temp.equals("string")) {
                            //if the type is string use strcmp
                            g.builder.append("    " + "%").append(g.counter++).append(" = call i32 @strcmp(i8* ").append(firstExp.value).append(", i8* ").append(secondExp.value).append(")\n");
                            g.builder.append("    " + "%").append(g.counter).append(" = icmp eq i32 %").append(g.counter - 1).append(", 0\n");
                            this.value="%" + g.counter++;
                        } else {
                            //otherwise (it is a class) cast the pointer into a int64 a compare it
                            g.builder.append("    " + "%").append(g.counter++).append(" = ptrtoint ").append(g.typeConversion(temp)).append(" ").append(firstExp.value).append(" to i64\n");
                            g.builder.append("    " + "%").append(g.counter++).append(" = ptrtoint ").append(g.typeConversion(typeR)).append(" ").append(secondExp.value).append(" to i64\n");
                            g.builder.append("    " + "%").append(g.counter).append(" = icmp eq i64 %").append(g.counter - 1).append(", %").append(g.counter - 2).append("\n");
                            this.value="%" + String.valueOf(g.counter++);
                        }
                    }
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

        String firstType = firstExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
        String secondType = secondExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
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

