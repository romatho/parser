package parserClasses;

public class Program extends Node {

    private ParserArray<Classe> myClasses;

    public Program(int pColumn, int pLine, ParserArray<Classe> pMyClasses)
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
