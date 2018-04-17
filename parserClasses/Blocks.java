package parserClasses;

public class Blocks extends Expressions{
        private ParserArray<Expressions> exprs;



        public Blocks( int pColumn, int pLine, ParserArray<Expressions> exprs)
        {
            super(pColumn,pLine);
            this.exprs = exprs;
        }

    @Override
    public String toString()
    {
        if(exprs.size() == 1)
            return this.exprs.get(0).toString();
        return this.exprs.toString();

    }

    @Override
    public String getType()
    {
        for(int i=0; i<exprs.size()-1;i++)
        {
            exprs.get(i).getType();
        }
        return exprs.get(exprs.size()-1).getType();
    }

}
