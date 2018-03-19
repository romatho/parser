package parserClasses;

public class Class extends Node {

    public String name;
    public String parentClass;
    public  Field myFields[];
    public Method[] myMethods;



    public String getDisplayString()
    {
        String toDisplay = "Class(" + name + ", " + parentClass + ", " + getFieldsString + ", " + getMethodsString + ")";
        return  toDisplay;
    }

    private String getFieldsString()
    {
        String toDisplay ="[";
        for(int i =0; i < myFields.length - 1; i++)
        {
            toDisplay += myFields[i].getDisplayString();
            toDisplay += ", ";
        }
        if(myFields.length != 0)
            toDisplay += myFields[myFields.length-1].getDisplayString();
        toDisplay += "]";
        return toDisplay;
    }
    private String getMethodsString()
    {
        String toDisplay ="[";
        for(int i =0; i < myMethods.length - 1; i++)
        {
            toDisplay += myMethods[i].getDisplayString();
            toDisplay += ", ";
        }
        if(myMethods.length != 0)
            toDisplay += myMethods[myMethods.length-1].getDisplayString();
        toDisplay += "]";
        return toDisplay;
    }
}
