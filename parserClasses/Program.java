package parserClasses;

import llvm.Generator;

import java.util.Collection;
import java.util.LinkedList;

public class Program extends Node {

    private ParserArray<Classe> myClasses;
    public StringBuilder output;

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
    public void toLlvm(Generator g) {
        g.methodsBuilder = new LinkedList<>();
        g.declarationsBuilder = new LinkedList<>();

        // Traverse the different classes (except IO as it has been hardcoded)
        for(Classe c : g.c.allowedClasses.values())
            if(!c.getName().equals("IO"))
                c.toLlvm(g);
    }

    public ParserArray<Classe> getClasses()
    {
        return myClasses;
    }
}
