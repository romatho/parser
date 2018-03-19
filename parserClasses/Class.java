package parserClasses;

public class Class extends  Node{

    private String name;
    private String parentClass;
    private ParserArray<Field> myFields;
    private ParserArray<Method> myMethods;

    public Class(int pColumn, int pLine, String pName, String pParentClass, ParserArray<Field> pMyFields, ParserArray<Method> pMyMethods)
    {
        super(pColumn,pLine);
        name = pName;
        parentClass = pParentClass;
        myFields = pMyFields;
        myMethods = pMyMethods;
    }

    @Override
    public String toString()
    {
        String toDisplay = "Class(" + name + ", " + parentClass + ", " + myFields.toString() + ", " + myMethods.toString() + ")";
        return  toDisplay;
    }

}
