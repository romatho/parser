package parserClasses;

public class Types extends Node {
    private String type;
    public Types(int pColumn, int pLine, String type)
    {
        super(pColumn,pLine);
        this.type= type;
    }
    @Override
    public String toString()
    {
        return type;
    }

}