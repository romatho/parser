package parserClasses;

public class Classe extends  Node{

    private String name;
    private String parentClasse;
    private ClassBody body;
    public Classe(int pColumn, int pLine, String pName, ClassBody body)
    {
        super(pColumn,pLine);
        name = pName;
        parentClasse = "Object";
        this.body=body;

    }

    public Classe(int pColumn, int pLine, String pName, String pParentClasse,  ClassBody body)
    {
        super(pColumn,pLine);
        name = pName;
        parentClasse = pParentClasse;
        this.body=body;
    }

    @Override
    public String toString()
    {
        String toDisplay = "Classe(" + name + ", " + parentClasse + ", " +body.toString() + ")";
        return  toDisplay;
    }

}
