package parserClasses;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Method extends Node{

    private ParserArray<Formals> formals;
    private String Identifier;
    private Types ReturnType;
    private Blocks block;


    public Method(int pColumn, int pLine, String Identifier, ParserArray<Formals> formals,
                  Types ReturnType, Blocks expr)
    {
        super(pColumn, pLine);
        this.Identifier = Identifier;
        this.formals = formals;
        this.ReturnType = ReturnType;
        this.block = expr;
    }


    @Override
    public String toString(boolean checkerMode) {
        return "Method(" + Identifier + ", " + formals.toString(checkerMode)
                + ", " + ReturnType.toString(checkerMode) + ", " + block.toString(checkerMode) + ")";
    }

    public String getIdentifier() {
        return Identifier;
    }
    public ParserArray<Formals> getFormals() {
        return formals;
    }

    public void getType(HashMap<String, HashMap<String, String>> classFieldType,
                   HashMap<String, HashMap<String, String> > classMethodType,
                   HashMap<String, HashMap<String, ArrayList< Map.Entry<String, String> >> > classMethodFormalsType, String classe)
    {
        this.block.getType(classFieldType, classMethodType, classMethodFormalsType, new HashMap<String,String>() , classe, Identifier);
    }

    public String getReturnType() {
        return ReturnType.getType();
    }
}