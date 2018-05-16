package parserClasses;


import check.*;
import llvm.Generator;
import java.util.*;

public class Terminal extends Expressions{
    public String type;
    private String pvalue;
    private String classe;

    public Terminal(int pColumn, int pLine, String pValue,String pType)
    {
        super(pColumn, pLine);
        this.pvalue = pValue;
        type= pType;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        String toDisplay = pvalue;
        if(checkerMode)
            toDisplay += " : " + type;
        return toDisplay;
    }

    @Override
    public String getType( HashMap<String, HashMap<String, String>> classFieldType,
                           HashMap<String, HashMap<String, String> > classMethodType,
                           HashMap<String, HashMap<String, ArrayList< Pair >> > classMethodFormalsType,
                           HashMap<String,String> localVariables, String classe, String filename, String methode, Checker c, boolean fieldExpr) {
       this.classe=classe;
        // Check that we're not in a field init expression and that we're using another field of the class to init it
        if(fieldExpr && classFieldType.get(classe).containsKey(pvalue))
        {
            System.err.println(filename +":" + this.displayNode() +
                    "semantic error: cannot use class fields in field initializers.");
            c.toReturn = 1;
            type = "ERROR";
            return type;
        }

        if(pvalue.equals("self"))
        {
            type= classe;
            return type;
        }
        if (type!="SELF"&&type!="OI")
            return type;

        else if (type.equals("OI")) {
            if (localVariables.get(pvalue) == null) {
                ArrayList<Pair> temp = classMethodFormalsType.get(classe).get(methode);
                if (temp != null) {
                    int i = 0;
                    for (i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getKey().equals(pvalue)) {
                            type = temp.get(i).getValue();
                            return type;
                        }

                    }

                }
                    if (classFieldType.containsKey(pvalue)) {
                        type = pvalue;
                        return type;
                    }
                    if (classFieldType.get(classe).get(pvalue) != null)

                    {
                        type = classFieldType.get(classe).get(pvalue);
                        return type;
                    }



            } else {
                type = localVariables.get(pvalue);
                return type;

            }

        }
        if(type.equals("SELF"))
        {
            type= classe;
            return type;
        }
        c.toReturn=1;

        System.err.println(filename +":" + this.displayNode() + "semantic error: " + this.pvalue + " is undefined pute ");
        type="ERROR";
        return "ERROR";
    }
    @Override
    public void toLlvm(Generator g) {
        if (this.type.equals("int32")||this.type.equals("bool")||this.type.equals("unit"))
            this.value=this.pvalue;
        if (!this.type.equals("int32")&&!this.type.equals("bool")&&!this.type.equals("string")&&!this.type.equals("unit")) {
            String temp = this.type.replace(" : ", "");

            if (g.vars.get(this.pvalue) != null) {
                // If it is in the vartable just load its pvalue
                g.builder.append("    " + "%").append(g.counter).append(" = load ").append(g.typeConversion(temp)).append("* ").append(g.vars.get(this.pvalue)).append("\n");
               this.value="%" + g.counter++;
            } else {
                ArrayList fields =Arrays.asList( g.c.classFieldType.keySet().toArray());
                int pos=0;
                for(Object elements:fields) {
                    if (elements.equals(this.pvalue))
                        break;
                    pos++;
                }
                g.vars.put(this.pvalue, "%" + g.counter);
                g.builder.append("    " + "%").append(g.counter++).append(" = getelementptr inbounds ").append("%class.").append(this.classe).append("* %this, i32 0, i32 ").append(pos+1).append("\n");
                g.builder.append("    " + "%").append(g.counter).append(" = load ").append(g.typeConversion(this.type)).append("* %").append(g.counter - 1).append("\n");
                this.value="%" + g.counter++;
            }
        }
    }
}
