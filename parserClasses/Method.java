package parserClasses;


import check.*;
import llvm.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Method extends Node{

    private ParserArray<Formals> formals;
    private String Identifier;
    private Types ReturnType;
    private Blocks block;
    private String classe;

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

    @Override
    public void toLlvm(Generator g) {
        StringBuilder builder = new StringBuilder();
        StringBuilder alloc = new StringBuilder();
        StringBuilder store = new StringBuilder();
        String rtype = "";
        g.builder = new StringBuilder();
        g.vars = new HashMap<>();

        g.counter = 1;

        if (this.getReturnType().equals("unit"))
            rtype = "void";
        else
            rtype = g.typeConversion(this.getReturnType());

        builder.append("define ");
        builder.append(rtype);
        builder.append(" @").append(classe);
        builder.append(this.Identifier);

        builder.append("(").append(g.typeConversion(classe)).append(" %this");
        g.vars.put("self", "%0");
        // Store the value of this in a new var (%0)
        alloc.append("    " + "%0 = alloca ").append(g.typeConversion(classe)).append(" ;%this\n");
        store.append("    " + "store ").append(g.typeConversion(classe)).append(" %this, ").append(g.typeConversion(classe)).append("* %0\n");

        /* Store all the fields in new variable and put it in the g.vars
          to know where it stores the value of the variable*/
        if (this.formals != null) {
            builder.append(", ");
            ParserArray<Formals> array= this.getFormals();
            for (int i = 0; i <array.size(); i++) {
                String nameOfVar = array.get(i).getIdentifier();
                String type = g.typeConversion(array.get(i).getType());
                builder.append(type).append(" %").append(nameOfVar);
                alloc.append("    " + "%").append(g.counter).append(" = alloca ").append(type).append("\n");
                store.append("    " + "store ").append(type).append(" %").append(nameOfVar).append(", ").append(type).append("* ").append("%").append(g.counter).append("\n");
                g.vars.put(nameOfVar, "%" + g.counter++);

                if (i < array.size() - 1)
                    builder.append(", ");
            }
        }
        builder.append(")");
        // Add the entry label
        builder.append("{\nentry:\n");



            // First do all allocation
            builder.append(alloc);
            // Then store all values
            builder.append(store);
            this.block.toLlvm(g);


            String value;

            if (this.getReturnType().equals("unit")) {
                if (this.getReturnType().equals(this.block.type.replace(" : ", "")))
                    value = this.block.value;
                else {
                    // If the given type in argument isn't the right type, must do a bitcast of it
                    String eType = this.block.type.replace(" : ", "");
                    g.builder.append("    " + "%").append(g.counter).append(" = bitcast %class").append(eType).append("* ").append(this.block.value).append(" to %class.").append(this.getReturnType()).append("*\n");
                    value = "%" + g.counter++;
                }
            } else
                //void don't have a value
                value = "";

            g.builder.append("    " + "ret ").append(rtype).append(" ").append(value).append("\n");


        builder.append(g.builder).append("}\n");

        g.methodsBuilder.add(builder);
    }

    public String getIdentifier() {
        return Identifier;
    }
    public ParserArray<Formals> getFormals() {
        return formals;
    }

    public void getType(HashMap<String, HashMap<String, String>> classFieldType,
                   HashMap<String, HashMap<String, String> > classMethodType,
                   HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType, String classe, String filename, Checker c, boolean fieldExpr)
    {
        this.classe=classe;
        if(!ReturnType.getType().equals(this.block.getType(classFieldType, classMethodType, classMethodFormalsType, new HashMap<String,String>() , classe, filename, Identifier,c, fieldExpr)))

            if(!c.childHasParent(this.block.getType(classFieldType, classMethodType, classMethodFormalsType, new HashMap<String,String>() , classe, filename, Identifier,c, fieldExpr),ReturnType.getType()))
            {
            System.err.println(filename +":" + this.displayNode() +
                    "semantic error: return type for " +Identifier  + " is "+ReturnType.getType()+" not "+this.block.getType(classFieldType, classMethodType, classMethodFormalsType, new HashMap<String,String>() , classe, filename, Identifier,c, fieldExpr));
        c.toReturn=1;
        }
    }

    public String getReturnType() {
        return ReturnType.getType();
    }
}