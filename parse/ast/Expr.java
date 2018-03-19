package parse.ast;

public class Expr extends Node {

    public Expr into;

    Expr(int pColumn, int pLine, Expr pExpr)
    {
        super(pColumn, pLine);
        into = pExpr;
    }

    public void display()
    {
        System.out.println(displayNode());
        if(into == null)
        {
            System.out.println("into is null");
        }
        else
        {
            into.display();
        }
    }


}
