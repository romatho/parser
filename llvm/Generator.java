package llvm;

import check.Checker;
import parserClasses.Classe;
import parserClasses.Formals;
import parserClasses.Method;

import java.util.HashMap;

public class Generator {
    public int counter;
    public int ifCounter;
    public Checker c;

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
                return "%classe." + initType + "*";
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

    private StringBuilder vTableToString(Classe current)
    {
        String comma = "";
        StringBuilder classObject = new StringBuilder("%classe." + current.getName() + " = type{ %table." + current.getName() + "VTable}");
        StringBuilder vTableString = new StringBuilder("%table." + current.getName() + "VTable = type { ");
        StringBuilder vTableGlobalString = new StringBuilder("@" + current.getName() + "VTableGlobal = type { ");

        for(Method method : current.getBody().getMyMethods())
        {
            StringBuilder toAdd = new StringBuilder();
            toAdd.append(comma + typeConversion(method.getReturnType() + " ("));
            //add the class itself as formal
            toAdd.append("%classe." + current.getName() + "*");
            //add formals
            for(Formals formal : method.getFormals())
                toAdd.append(", " + typeConversion(formal.getType()));
            toAdd.append(")*");
            comma = ",";

            vTableString.append(toAdd);
            vTableGlobalString.append(toAdd + " @" + current.getName() + method.getIdentifier());
        }
        return classObject.append("\n" + vTableString.append( "\n" + vTableGlobalString +  "\n"));
    }

    //add the stdin from c
    String addStdIn()
    {
        //TODO: ne focntionne peut etre pas
        return "%file = type { i32, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, %marker*, %file*, i32, i32, i64, i16, i8, [1 x i8], i8*, i64, i8*, i8*, i8*, i8*, i64, i32, [20 x i8] }" +
        "\n %marker = type { %marker*, %file*, i32 }";
    }


    public void createLLVM()
    {
        builder = new StringBuilder();
        //target triple for compilation with clang
        builder.append(getTargetTriple());
        //vTable for Io and Object
        builder.append(vTableToString(c.objectClass) + "\n");
        builder.append(vTableToString(c.IOClass) + "\n");
        //vTable for classes in the file
        for(Classe current: c.p.getClasses())
            builder.append(vTableToString(current) + "\n");
        //TODO : add toLLVM()
    }


}
