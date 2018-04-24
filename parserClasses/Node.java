package parserClasses;

public abstract class Node {

    public int line;
    public int column;

    Node(int pColumn, int pLine)
    {
        column = pColumn;
        line = pLine;
    }

    public abstract String toString(boolean checkerMode);

    public String displayNode()
    {
        return new String(column + ":" + line + ":" );

    }
}
