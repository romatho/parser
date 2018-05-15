package parserClasses;


import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Terminal extends Expressions{
    private String type;
    private String value;

    public Terminal(int pColumn, int pLine, String pValue,String pType)
    {
        super(pColumn, pLine);
        value = pValue;
        type= pType;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = value;
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr) {
        // Check that we're not in a field init expression and that we're using another field of the class to init it
        if(fieldExpr && classFieldType.get(classe).containsKey(value))
        {
            System.err.println(filename +":" + this.displayNode() +
                    "semantic error: cannot use class fields in field initializers.");
            c.toReturn = 1;
            type = "ERROR";
            return type;
        }

        if(value.equals("self"))
        {
            type= classe;
            return type;
        }
        if (type!="SELF"&&type!="OI")
            return type;

        else if (type.equals("OI")) {
            if (localVariables.get(value) == null) {
                ArrayList<Pair> temp = classMethodFormalsType.get(classe).get(methode);
                if (temp != null) {
                    int i = 0;
                    for (i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getKey().equals(value)) {
                            type = temp.get(i).getValue();
                            return type;
                        }

                    }

                }
                    if (classFieldType.containsKey(value)) {
                        type = value;
                        return type;
                    }
                    if (classFieldType.get(classe).get(value) != null)

                    {
                        type = classFieldType.get(classe).get(value);
                        return type;
                    }



            } else {
                type = localVariables.get(value);
                return type;

            }

        }
        if(type.equals("SELF"))
        {
            type= classe;
            return type;
        }
        c.toReturn=1;

        System.err.println(filename +":" + this.displayNode() + "semantic error: " + this.value + " is undefined pute ");
        type="ERROR";
        return "ERROR";
    }
    @Override
    public String toLlvm(Generator g)
    {
       return value;
    }
}
