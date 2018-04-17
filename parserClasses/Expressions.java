package parserClasses;

public abstract class Expressions extends Node {

    public Expressions(int pColumn, int pLine)
    {
        super(pColumn, pLine);
    }

    public abstract String toString(boolean checkerMode);

    public abstract String getType();

}
