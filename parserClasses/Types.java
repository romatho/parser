package parserClasses;

import llvm.Generator;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Types extends Node {
    public String type;
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

    @Override
    public String toLlvm(Generator g) {
        return null;
    }


    public String getType( ) {
        return type;
    }
}
