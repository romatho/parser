package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Expressions extends Node {

    public Expressions(int pColumn, int pLine)
    {
        super(pColumn, pLine);
    }

    public abstract String toString(boolean checkerMode);

    public abstract String getType( HashMap<String, HashMap<String, String>> classFieldType,
             HashMap<String, HashMap<String, String> > classMethodeType,
             HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                                    HashMap<String,String> localVariables);

}
