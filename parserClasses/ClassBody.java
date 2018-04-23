package parserClasses;

public class ClassBody extends  Node{

    private ParserArray<Field> myFields;
    private ParserArray<Method> myMethods;

    public ClassBody(int pColumn, int pLine)
    {
        super(pColumn,pLine);
        this.myFields= new ParserArray<>();
        this.myMethods= new ParserArray<>();
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
    public String toString(boolean checkerMode)
    {
        return myFields.toString(checkerMode)+ ", " + myMethods.toString(checkerMode);
    }


    public ParserArray<Method> getMyMethods()
    {
        return myMethods;
    }
    public ParserArray<Field> getMyFields()
    {
        return myFields;
    }

}
