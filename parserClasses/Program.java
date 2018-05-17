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
        output = new StringBuilder();

        // Main function
        output.append("define i32 @main() {").append("\n");
        output.append("    %1 = call %class.Main* @Main-new()\n");
        output.append("    %2 = call i32 @Main-main(%class.Main* %1)\n");
        output.append("    ret i32 %2\n}\n\n");
        g.methodsBuilder = new LinkedList<>();
        g.declarationsBuilder = new LinkedList<>();
        g.methodsBuilder.add(output);

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
