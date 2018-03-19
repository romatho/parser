package parserClasses;

public class Program extends Node {

    public ParserArray<Class> myClasses;

    public Program(int pColumn, int pLine, ParserArray<Class> pMyClasses)
    {
        super(pColumn,pLine);
        myClasses = pMyClasses;
    }

    @Override
    public String toString()
    {
        String toDisplay = "[";
        toDisplay += myClasses.toString();
        toDisplay += "]";
        return toDisplay;
    }
}
