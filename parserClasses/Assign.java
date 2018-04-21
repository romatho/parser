package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Assign extends Expressions{

    private String objectIdentifier;
    private Expressions exp;
    private String type = null;


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
        toDisplay += exp.toString();
        if(checkerMode)
            toDisplay += " : " + type;
        toDisplay += ")";
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                          HashMap<String, HashMap<String, String> > classMethodType,
                          HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType, HashMap<String,String> localVariables, String classe, String filename, String methode)
    {
        if(type != null)
            return  type;

        type = exp.getType(classFieldType, classMethodType, classMethodFormalsType, localVariables, classe, filename, methode);

        if(type.equals("ERROR"))
            return type;

        // check if the object identifier is defined somewhere and if it has the same type as the assigned expression
        if(localVariables.get(objectIdentifier) == null)
        {
            if(classFieldType.get(classe).get(objectIdentifier) == null)
            {
                ArrayList<Pair<String, String>> temp = classMethodFormalsType.get(classe).get(methode);
                int i = 0;
                for (i = 0; i < temp.size(); i++)
                {
                    if(temp.get(i).getKey().equals(objectIdentifier))
                    {
                        if(temp.get(i).getValue().equals(type))
                            return type;
                    }
                }

                System.err.println(filename +":" + this.displayNode() +
                        "SEMANTIC error: " + objectIdentifier + " is undefined");
                type = "ERROR";
                return "ERROR";
            }
            else
            {
                if(classFieldType.get(classe).get(objectIdentifier).equals(type))
                    return type;
            }
        }

        if(!localVariables.get(objectIdentifier).equals(type))
            type = "ERROR";

        return type;
    }
}
