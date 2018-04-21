package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

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
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode) {

        if (type.equals("bool") || type.equals("int32") ||
                type.equals("string") || type.equals("unit"))
            return type;

        else if (type.equals("OI")) {
            if (localVariables.get(value) == null) {
                if (classFieldType.get(classe).get(value) == null) {
                    ArrayList<Pair<String, String>> temp = classMethodFormalsType.get(classe).get(methode);

                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getKey().equals(value)) {
                            type = temp.get(i).getValue();
                            return type;
                        }
                    }
                    if(type.equals("SELF")){
                        type= classe;
                        return type;
                    }

                    System.err.println(filename +":" + this.displayNode() + "SEMANTIC error: " + this.value + " is undefined");
                    return "ERROR";
                }
                else
                {
                    type = classFieldType.get(classe).get(value);
                    return type;
                }
            } else {
                type = localVariables.get(value);
                return type;

            }
        }

        return type;
    }

}
