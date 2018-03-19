package parserClasses;

public class Program {

    public ParserArray<Class> myClasses;

    @Override
    public String toString()
    {
        String toDisplay = "[";
        toDisplay += myClasses.toString();
        toDisplay += "]";
    }
}
