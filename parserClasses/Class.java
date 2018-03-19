package parserClasses;

public class Class {

    public String name;
    public String parentClass;
    public ParserArray<Field> myFields;
    public ParserArray<Method> myMethods;

    @Override
    public String toString()
    {
        String toDisplay = "Class(" + name + ", " + parentClass + ", " + myFields.toString() + ", " + myMethods.toString() + ")";
        return  toDisplay;
    }

}
