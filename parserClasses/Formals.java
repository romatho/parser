package parserClasses;

import llvm.Generator;

public class Formals extends Node {
    private String identifier;
    private Types type;


    public Formals(int pColumn, int pLine, String indentifier, Types type) {
        super(pColumn, pLine);
        this.identifier = indentifier;
        this.type = type;
    }


    @Override
    public String toString(boolean checkerMode)
    {
        return  identifier + " : " + type.toString(false) ;
    }

    @Override
    public void toLlvm(Generator g) {
        return null;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type.getType();
    }
}