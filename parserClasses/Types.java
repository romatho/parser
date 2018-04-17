package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Types extends Node {
    private String type;
    public Types(int pColumn, int pLine, String type)
    {
        super(pColumn,pLine);
        this.type= type;
    }
    @Override
    public String toString(boolean checkerMode)
    {
        return type;
    }


    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodeType,
                           HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                           HashMap<String,String> localVariables) {
        return type;
    }
}
