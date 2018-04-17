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


    public String getType( ) {
        return type;
    }
}
