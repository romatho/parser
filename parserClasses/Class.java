package parserClasses;

public class Class extends  Node{

    public String name;
    public String parentClass;
    public ParserArray<Field> myFields;
    public ParserArray<Method> myMethods;

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
