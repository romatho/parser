package parserClasses;


import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class Assign extends Expressions{

    private String objectIdentifier;
    private Expressions exp;
    public String type = null;


    public Assign(int pColumn, int pLine, String pObjectIdentifier, Expressions pExp)
    {
        super(pColumn, pLine);
        objectIdentifier = pObjectIdentifier;
        exp = pExp;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = "Assign(";
        toDisplay += objectIdentifier;
        toDisplay += ", ";
        toDisplay += exp.toString(checkerMode);
        toDisplay += ")";
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public void toLlvm(Generator g) {
        this.exp.toLlvm(g);
        String exp = this.exp.value;

        if(type.equals("unit")) {
           return;
        }

        String as = g.varTable.get(objectIdentifier);
        if(as == null) {
            ObjectTable obj = classTable.get(currentClass);

            g.varTable.put(objectIdentifier, "%" + g.counter++);
            g.builder.append("    %").append(g.counter).append(" = getelementptr inbounds %class.").append(currentClass)
                    .append("* %this, i32 0, i32 ").append(obj.getIndexOfVar(objectIdentifier)).append("\n");
            as = "%" + g.counter++;
        }
        g.builder.append("    store ").append(g.typeConversion(type)).append(" ").append(exp).append(", ")
                .append(g.typeConversion(type)).append("* ").append(as).append("\n");
        value = exp;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                          HashMap<String, HashMap<String, String> > classMethodType,
                          HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType, HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr)
    {
        if(type != null)
            return  type;

        type = exp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe,filename, methode, c, fieldExpr);
        if(type.equals("ERROR"))
        {
            c.toReturn=1;
            return type;
        }

        // check if the object identifier is defined somewhere and if it has the same type as the assigned expression
        if(localVariables.get(objectIdentifier) == null) {

            ArrayList<Pair> temp = classMethodFormalsType.get(classe).get(methode);
            if (temp != null) {
                int i = 0;
                for (i = 0; i < temp.size(); i++) {
                    if (temp.get(i).getKey().equals(objectIdentifier)) {
                        if (temp.get(i).getValue().equals(type)) {
                            
                            return type;
                        }

                    }

                }

            }

                    if (classFieldType.get(classe).get(objectIdentifier) != null) {
                        if (classFieldType.get(classe).get(objectIdentifier).equals(type))
                            return type;
                    } else {
                        System.err.println(filename + ":" + this.displayNode() +
                                "semantic error: " + objectIdentifier + " is undefined");
                        type = "ERROR";
                        c.toReturn = 1;
                        return "ERROR";
                    }





        }


        if(!localVariables.get(objectIdentifier).equals(type))
        {
            c.toReturn=1;
            type = "ERROR";
        }

        return type;
    }
}
