package parserClasses;

import llvm.Generator;

public abstract class Node {

    public int line;
    public int column;

    Node(int pColumn, int pLine)
    {
        column = pColumn;
        line = pLine;
    }

    public abstract String toString(boolean checkerMode);
    public abstract String toLlvm(Generator g);

    public String displayNode()
    {
        return new String(column + ":" + line + ":" );

    }
}
