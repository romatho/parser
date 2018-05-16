package parserClasses;

import llvm.Generator;

public abstract class Node {

    public int line;
    public int column;
    public String value;

    Node(int pColumn, int pLine)
    {
        column = pColumn;
        line = pLine;
    }

    public abstract String toString(boolean checkerMode);
    public abstract void toLlvm(Generator g);

    public String displayNode()
    {
        return new String(column + ":" + line + ":" );

    }
}
