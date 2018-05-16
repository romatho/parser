package llvm;

import parserClasses.Classe;
import parserClasses.Formals;
import parserClasses.Method;

import java.util.HashMap;

public class Generator {
    public int counter;
    public int ifCounter;
    public HashMap<String, String> vars;
    public StringBuilder builder;

    /**
     * Converts a VSOP type to a LLVM type
     * @param initType string containing the VSOP type
     * @return the corresponding LLVM type in a string
     */
    public String typeConversion(String initType) {
        switch(initType) {
            case "bool":
                return "i1";

            case "int32":
                return "i32";

            case "string":
                return "i8*";

            case "unit":
                return "i1";

            default:
                return "%class." + initType + "*";
        }
    }


    public String getTargetTriple()
    {
        String targetTriple = "target triple " + System.getProperty("os.arch");
        String lowerOsName = System.getProperty("os.name").toLowerCase();
        if (lowerOsName.contains("mac"))
            targetTriple += "-apple-macosx";
        else if (lowerOsName.contains("win"))
            targetTriple += "-pc-windows";
        else
            targetTriple += "-pc-linux-gnu";
        return targetTriple;
    }

    StringBuilder vTableToString(Classe current)
    {
        String comma = "";
        StringBuilder vTableString = new StringBuilder("%table." + current.getName() + "VTable = type { ");
        StringBuilder vTableGlobalString = new StringBuilder("@" + current.getName() + "VTableGlobal = type { ");

        for(Method method : current.getBody().getMyMethods())
        {
            StringBuilder toAdd = new StringBuilder();
            toAdd.append(comma + typeConversion(method.getReturnType() + " ("));
            //add the class itself as formal
            toAdd.append("%class." + current.getName() + "*");
            //add formals
            for(Formals formal : method.getFormals())
                toAdd.append(", " + typeConversion(formal.getType()));
            toAdd.append(")*");
            comma = ",";

            vTableString.append(toAdd);
            vTableGlobalString.append(toAdd + " @" + current.getName() + method.getIdentifier());
        }
        return vTableString.append( "\n" +vTableGlobalString);
    }


}
