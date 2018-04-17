package parserClasses;

public class Blocks extends Expressions{
        private ParserArray<Expressions> exprs;



        public Blocks( int pColumn, int pLine, ParserArray<Expressions> exprs)
        {
            super(pColumn,pLine);
            this.exprs = exprs;
        }

    @Override
    public String toString(boolean checkerMode)
    {
        if(exprs.size() == 1)
            return this.exprs.get(0).toString(checkerMode);
        return this.exprs.toString(checkerMode);

    }

    @Override
    public String getType()
    {
        return exprs.get(exprs.size()-1).getType();
    }

}
