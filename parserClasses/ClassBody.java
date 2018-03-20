package parserClasses;

public class ClassBody {

    private ParserArray<Field> myFields;
    private ParserArray<Method> myMethods;

    public ClassBody()
    {
        myFields= new ParserArray<Field>();
        myMethods= new ParserArray<Method>();
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
