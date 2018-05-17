package parserClasses;


import check.*;
import llvm.Generator;
import llvm.StringHandler;

import java.util.*;

public class Call extends Expressions {

    private Expressions objectExp;
    private String methodName;
    private ParserArray<Expressions> listExp;
    public String type;
    private String calltype;
    private String objectType;
    private String classe;

    public Call(int pColumn, int pLine, Expressions pObjectExp, String pMethodName, ParserArray<Expressions> pListExp)
    {
        super(pColumn, pLine);
        objectExp = pObjectExp;
        methodName = pMethodName;
        listExp = pListExp;
        type = null;
        calltype=null;
    }


    @Override
    public String toString(boolean checkerMode)
    {

        String toDisplay = "Call(";
        toDisplay += objectExp.toString(checkerMode);
        toDisplay += ", ";
        toDisplay += methodName;
        toDisplay += ", ";
        toDisplay += listExp.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + calltype;
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g) {
        String self = "%0";
        StringBuilder params = new StringBuilder();

        if(objectExp != null) {
            objectExp.toLlvm(g);
            self = objectExp.value;
        }

        int methodPos = 0;
        for(Object elem : g.c.classMethodType.keySet().toArray()) {
            if(elem.equals(objectType))
                break;
            methodPos++;
        }

        int formalPos = 0;
        for(Object elem:g.c.classMethodFormalsType.keySet().toArray()) {
            if(elem.equals(objectType))
                break;
            formalPos++;
        }
        if(listExp != null) {
            for(Expressions exp : listExp) {
                exp.toLlvm(g);

                String str = exp.value;
                if(exp.type.equals("unit"))
                    str = "0";
                else {
                    if(g.strings.containsKey(exp)) {
                        StringHandler string = g.strings.get(exp);
                        str = "getelementptr inbounds (" + string.size + "* " + string.identifier + ", i32 0, i32 0)";
                    }
                }
                params.append(", ").append(g.typeConversion(exp.type)).append(" ").append(str);
            }
        }

        if(objectExp == null) {
            self = "%" + g.counter++;
            g.builder.append("    ").append(self).append(" = load %class.").append(classe).append("** %0\n");
        }
        String convType;
        if(!type.equals("unit"))
            convType = g.typeConversion(type);
        else
            convType = "void";

        HashMap<String, Field> fields = g.c.allowedField.get(classe);
        String fieldsString = fields.toString();

        g.builder.append("    %").append(g.counter++).append(" = getelementptr inbounds %class.").append(objectType).append("* ").append(self).append(", i32 0, i32 0\n");
        g.builder.append("    %").append(g.counter++).append(" = load %struct.").append(objectType).append("_vtable** %").append(g.counter - 2).append("\n");
        g.builder.append("    %").append(g.counter++).append(" = getelementptr inbounds %struct.").append(objectType).append("_vtable* %").append(g.counter - 2).append(", i32 0, i32 ").append(methodPos).append("\n");

        g.builder.append("    %").append(g.counter++).append(" = load ").append(convType).append(" ").append(fieldsString).append("** %").append(g.counter - 2).append("\n");
        String callName = "%" + (g.counter - 1);

        g.builder.append("    ");

        if(!convType.equals("void")) {
            g.builder.append("%").append(g.counter).append(" = ");
            value = "%" + g.counter++;
        }

        g.builder.append("call ").append(convType).append(" ").append(callName).append(" (").append(g.typeConversion(objectType)).append(" ").append(self);
        g.builder.append(params);
        g.builder.append(")").append("\n");
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        this.classe = classe;
        if(type != null)
            return type;

        if(fieldExpr && classMethodType.get(classe).containsKey(methodName))
        {
            System.err.println(filename +":" + objectExp.displayNode() +
                    " cannot use self in field initializer.");
            c.toReturn = 1;
            type = "ERROR";
            return type;
        }

        String objectType = objectExp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
        this.objectType = objectType;
        if(objectType.equals("ERROR"))
        {
            type = "ERROR";
            c.toReturn=1;
            return type;
        }
        // check if the call is really made on a method of a class
        if(objectType.equals("bool") || objectType.equals("int32") ||
                objectType.equals("string") || objectType.equals("unit"))
        {
            System.err.println(filename +":" + objectExp.displayNode() +
                    "semantic error: A variable of type " + objectType + " cannot have a method.");
            type = "ERROR";
            c.toReturn=1;
            return type;
        }
        // check if the class contains the specified method or not
        if((objectType.equals("SELF") && classMethodType.get(classe).get(methodName) == null) ||
                ((!objectType.equals("SELF") && classMethodType.get(objectType).get(methodName) == null)))
//        if(classMethodType.get(objectType).get(methodName) == null)
        {
            System.err.println(filename +":" + objectExp.displayNode() +
                    "semantic error: An object of class " + objectType + " doesn't have a method " + methodName);
            c.toReturn=1;
            type = "ERROR";
            return type;
        }

        int nbCallArgs = listExp.size();
        int nbDefArgs = classMethodFormalsType.get(objectType).get(methodName).size();
        // check if the number of arguments given to the function call is the same as in its definition
        if(nbCallArgs != nbDefArgs)
        {
            c.toReturn = 1;
            System.err.println(filename +":" + objectExp.displayNode() +
                    "semantic error: method " + methodName + " of class " + objectType +
                    " expects " + nbDefArgs + " arguments, but " + nbCallArgs + " were provided.");
            type = "ERROR";
            return type;
        }

        // check if the arguments given to the function have the same type as the formals
        int i = 0;
        for(Expressions e : listExp)
        {
            if(e.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr).equals("ERROR"))
            {
                c.toReturn=1;
                type = "ERROR";
                return type;
            }
            String argType = e.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
            Pair argument;
            if(objectType.equals("SELF"))
                argument = classMethodFormalsType.get(classe).get(methodName).get(i);
            else
                argument = classMethodFormalsType.get(objectType).get(methodName).get(i);


            if(!argument.getValue().equals(argType))
            {
                if(!c.childHasParent(argType,argument.getValue())) {
                    System.err.println(filename + ":" + objectExp.displayNode() +
                            "semantic error: The argument " + argument.getKey() + " must be of type " +
                            argument.getValue() + " and not " + argType);
                    c.toReturn = 1;
                    type = "ERROR";
                    return type;
                }
                type = argument.getValue();
                calltype = classMethodType.get(objectType).get(methodName);

                return type;
            }
            ++i;
        }

        // The method call is valid. Return the type defined for the method.
        calltype = classMethodType.get(objectType).get(methodName);
        type = classMethodType.get(objectType).get(methodName);

        return type;
    }
}