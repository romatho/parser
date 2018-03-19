package parserClasses;

public class Class extends Node {

    public String name;
    public String parentClass;
    public  Field myFields[];
    public Method[] myMethods;



    public String getDisplayString()
    {
        String toDisplay = "Class(" + name + ",";
        return  toDisplay;
    }
}
