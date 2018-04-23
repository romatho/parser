package parserClasses;

import java.util.ArrayList;
import java.util.HashMap;

public class ParserArray<E extends Node> extends ArrayList<E>{

    public String toString(boolean checkerMode)
    {
        StringBuilder toDisplay = new StringBuilder("[");
        for(int i = 0; i < this.size() - 1; i++)
        {
            toDisplay.append(this.get(i).toString(checkerMode));
            toDisplay.append(", ");
        }
        if(this.size() != 0)
            toDisplay.append(this.get(this.size() - 1).toString(checkerMode));
        toDisplay.append("]");
        return toDisplay.toString();
    }

}
