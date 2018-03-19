package parserClasses;

public class Types extends Node {
    private String type;
    public Types(int pColumn, int pLine, String type)
    {
        super(pColumn,pLine);
        this.type= type;
    }
    public String getDisplayString()
    {
        return type;
    }

}
