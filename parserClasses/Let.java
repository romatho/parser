package parserClasses;


import java.util.ArrayList;
import java.util.HashMap;
import check.*;
import llvm.Generator;

public class Let extends Expressions{

    private String objectIdentifier;
    private Types type;
    private Expressions scope;
    private Expressions init;
    String retType = null;


    public Let(int pColumn, int pLine, String pObjectIdentifier, Types pType, Expressions pScope)
    {
        super(pColumn, pLine);
        objectIdentifier = pObjectIdentifier;
        type = pType;
        scope = pScope;
        init = null;
    }

    public Let(int pColumn, int pLine, String pObjectIdentifier, Types pType, Expressions pScope, Expressions pInit)
    {
        super(pColumn, pLine);
        objectIdentifier = pObjectIdentifier;
        type = pType;
        scope = pScope;
        init = pInit;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "Let(";
        toDisplay += objectIdentifier;
        toDisplay += ", ";
        toDisplay += type.toString(checkerMode);
        if(init != null)
        {
            toDisplay += ", ";
            toDisplay += init.toString(checkerMode);
        }
        toDisplay += ", ";
        toDisplay += scope.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + retType;
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g) {
        // Store the variables table previous to the let call, so we can return to it afterwards.
        HashMap<String, String> prevVars = g.vars;
        // Allocate memory
        g.builder.append("    %").append(g.counter).append(" = alloca ").append(g.typeConversion(retType)).append("\n");

        if(init != null) {
            init.toLlvm(g);
            value = init.value;

            // If the type is unit, the value must be 0.
            if(retType.equals("unit"))
                value = "0";
            // store the initial value
            g.builder.append("    store ").append(g.typeConversion(retType)).append(" ").append(value).append(", ")
                    .append(g.typeConversion(retType)).append("* %").append(g.counter).append("\n");
        }
        g.vars.put(objectIdentifier, "%" + g.counter++);

        scope.toLlvm(g);
        value = scope.value;
        g.vars = prevVars;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode,Checker c, boolean fieldExpr)
    {

        String objectType = type.getType();
        if(objectType.equals("ERROR"))
        {
            c.toReturn=1;
            retType = "ERROR";
            return "ERROR";
        }

        // If the type specified for the variable doesn't correspond to a default type
        // or a defined object type
        if(!objectType.equals("bool") && !objectType.equals("int32") && !objectType.equals("string") &&
                !objectType.equals("unit") && !classFieldType.containsKey(objectType) &&
                 !classMethodType.containsKey(objectType))
        {
            System.err.println(filename +":" + this.displayNode() +
                    "semantic error: use of undefined type " + objectType);
            retType = "ERROR";
            c.toReturn = 1;
            return "ERROR";
        }

        // The variable will be considered as a local variable in init and scope expressions
        String temp=null;
        if(localVariables.containsKey(objectIdentifier))
            temp=localVariables.get(objectIdentifier);
        localVariables.put(objectIdentifier,type.getType());

        // if init exists, check if its type corresponds to the one defined for the object
        if(init != null)
        {
            String initType = init.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode,c, fieldExpr);
            if(initType.equals("ERROR"))
            {
                c.toReturn=1;
                retType = "ERROR";
                return "ERROR";
            }
            if(!initType.equals(type.getType()))
            {
                System.err.println(filename +":" + this.displayNode() +
                        "semantic error: expected same type for the variable and its initialisation");
                c.toReturn=1;
                retType = "ERROR";
                return "ERROR";
            }
        }

        // check if there is an error in the expression of the scope with the defined variable
        String ltype="OI";
        if (localVariables.get(objectIdentifier) == null) {
            if(classFieldType.containsKey(objectIdentifier))
            {
                ltype = objectIdentifier;
                retType = ltype;
                return ltype;
            }
                if (classFieldType.get(classe).get(objectIdentifier) == null) {
                    ArrayList<Pair> temp2 = classMethodFormalsType.get(classe).get(methode);
                    if(temp != null)
                    {
                        int i =0;
                        for ( i = 0; i < temp2.size(); i++) {
                            if (temp2.get(i).getKey().equals(objectIdentifier)) {
                                ltype = temp2.get(i).getValue();
                            }
                        }

                    }
                }
                else
                {
                    ltype = classFieldType.get(classe).get(objectIdentifier);
                }
            } else {
                ltype = localVariables.get(objectIdentifier);

            }

        String scopeType = scope.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode, c, fieldExpr);
        if(temp!=null)
            localVariables.put(objectIdentifier,temp);
        else
            localVariables.remove(objectIdentifier);
        if(scopeType.equals("ERROR"))
        {
            c.toReturn=1;
            retType = "ERROR";
            return "ERROR";
        }

        retType = scopeType;
        return retType;
    }
}

    
