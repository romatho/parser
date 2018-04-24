package parserClasses;


import check.*;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Expressions extends Node {
    public Expressions(int pColumn, int pLine)
    {
        super(pColumn, pLine);
    }

    public abstract String toString(boolean checkerMode);

    public abstract String getType( HashMap<String, HashMap<String, String>> classFieldType,
             HashMap<String, HashMap<String, String> > classMethodType,
             HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                                    HashMap<String,String> localVariables, String classe, String filename,
                                    String methode, Checker c, boolean fieldExpr);

}
