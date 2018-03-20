package parserClasses;

public class Class extends  Node{

    private String name;
    private String parentClass;
    private ClassBody body;
    public Class(int pColumn, int pLine, String pName, ClassBody body)
    {
        super(pColumn,pLine);
        name = pName;
        parentClass = "Object";
        this.body=body;

    }

    public Class(int pColumn, int pLine, String pName, String pParentClass,  ClassBody body)
    {
        super(pColumn,pLine);
        name = pName;
        parentClass = pParentClass;
        this.body=body;
    }

    @Override
    public String toString()
    {
        String toDisplay = "Class(" + name + ", " + parentClass + ", " +body.toString() + ")";
        return  toDisplay;
    }

}
