package parserClasses;

import llvm.Generator;

public class Program extends Node {

    private ParserArray<Classe> myClasses;

    public Program(int pColumn, int pLine, ParserArray<Classe> pMyClasses)
    {
        super(pColumn,pLine);
        myClasses = pMyClasses;
        //System.out.println(this.toString());
    }

    @Override
    public String toString(boolean checkerMode)
    {
        return myClasses.toString(checkerMode);
    }

    @Override
    public String toLlvm(Generator g) {
        return null;
    }

    public ParserArray<Classe> getClasses()
    {
        return myClasses;
    }
}
