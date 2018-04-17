package parserClasses;

public class Program extends Node {

    private ParserArray<Classe> myClasses;

    public Program(int pColumn, int pLine, ParserArray<Classe> pMyClasses)
    {
        super(pColumn,pLine);
        myClasses = pMyClasses;
        System.out.println(this.toString());
    }

    @Override
    public String toString(boolean checkerMode)
    {
        return myClasses.toString(checkerMode);
    }

    public ParserArray<Classe> getClasses()
    {
        return myClasses;
    }
}
