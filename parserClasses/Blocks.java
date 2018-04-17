package parserClasses;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

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
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                          HashMap<String, HashMap<String, String> > classMethodeType,
                          HashMap<String, HashMap<String, ArrayList< Pair<String, String> >> > classMethodeFormalsType,
                          HashMap<String,String> localVariables)
    {
        for(int i=0; i<exprs.size()-1;i++)
        {
            exprs.get(i).getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);
        }
        return exprs.get(exprs.size()-1).getType(classFieldType, classMethodeType, classMethodeFormalsType, localVariables);
    }

}
