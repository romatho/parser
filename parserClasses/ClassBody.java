package parserClasses;

public class ClassBody extends  Node{

    private ParserArray<Field> myFields;
    private ParserArray<Method> myMethods;

    public ClassBody(int pColumn, int pLine)
    {
        super(pColumn,pLine);
        this.myFields= new ParserArray<Field>();
        this.myMethods= new ParserArray<Method>();
    }
    public void addField(Field field)
    {
       myFields.add(field);
    }
    public void addMethod(Method method)
    {
        myMethods.add(method);
    }
    @Override
    public String toString()
    {
        String toDisplay =  myFields.toString()+ ", " +myMethods.toString() ;
        return  toDisplay;
    }

}
