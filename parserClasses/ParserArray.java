package parserClasses;

import java.util.ArrayList;

public class ParserArray<E> extends ArrayList<E>{

    @Override
    public String toString()
    {
        String toDisplay ="[";
        for(int i =0; i < this.size() - 1; i++)
        {
            toDisplay += this.get(i).toString();
            toDisplay += ", ";
        }
        if(this.size() != 0)
            toDisplay += this.get(this.size()-1).toString();
        toDisplay += "]";
        return toDisplay;
    }
}
