package parserClasses;

public class Blocks extends Node{
        private ParserArray<Expressions> exprs;



        public Blocks( int pColumn, int pLine, ParserArray<Expressions> exprs)
        {
            super(pColumn,pLine);
            this.exprs = exprs;
        }

    @Override
    public String toString()
    {
            return this.exprs.toString();

    }

}
