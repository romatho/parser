package parserClasses;

import java.util.ArrayList;

public class ParserArray<E> extends ArrayList<E>{
    public ArrayList<E> myArray;

    public ParserArray()
    {
        myArray = new ArrayList<E>();
    }

    public String toDisplayString()
    {
        String toDisplay ="[";
        for(int i =0; i < myArray.size() - 1; i++)
        {
            toDisplay += myArray[i].toString();
            toDisplay += ", ";
        }
        if(myArray.size() != 0)
            toDisplay += myArray[myArray.size()-1].toString();
        toDisplay += "]";
        return toDisplay;
    }

}
