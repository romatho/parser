package llvm;

import check.Checker;
import parserClasses.Classe;
import parserClasses.Formals;
import parserClasses.Method;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {
    public int counter;
    public int ifCounter=0;
    public int stringCounter=0;
    public int whileCounter=0;
    public Checker c;

    public HashMap<String, String> vars;
    public StringBuilder builder;
    public StringBuilder stringBuilder;
    public LinkedHashMap<String, stringHandler> strings;

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

    StringBuilder automaticIOCreation()
    {
        // The implementaiton is done thankss to the definition of the class in the VSOP manual

        StringBuilder toReturn = new StringBuilder();

        //printf
        String methodPrintf = "declare i32 @printf(i8*, ...)\n\n";

        //printInt32 method
        String methodPrintInt32 = "define %classe.IO* @IOPrintInt32(%classe.IO* %this, i32 %myInt32){\n" +
                "entry:\n"+
                "%1 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([3 x i8]* @formatInt , i32 0, i32 0), i32 %myInt32)" +
                "\tret %class.IO* %this\n" +
                "}\n";


        //printBool method
        String methodPrintBool = "define %classe.IO* @IOPrintBool(%classe.IO* %this, i1 %myBool){\n" +
                "entry:\n"+
                "\t%1 = icmp eq i1 %myBool, 0\n"+
                "\tbr = i1 %1 label %labelFalse, label %labelTrue \n"+
                "labelFalse:\n"+
                "%2 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @false , i32 0, i32 0))" +
                "\tret %class.IO* %this" +
                "labelTrue:\n"+
                "%3 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @true , i32 0, i32 0))\n" +
                "\tret %class.IO* %this\n" +
                "}\n";


        //print method
        String methodPrint = "define %classe.IO* @IOPrint(%classe.IO* %this, i8 %myString){\n" +
                "entry:\n" +
                "\t%1 = call i32(i8*,...)* @printf(i8 %myString)\n" +
                "\tret classe.IO* %this\n" +
        "}\n";


        //
        String methodInputInt32 = "";

        //
        String methodInputBool = "";

        //
        String methodInputLine = "";


        return toReturn.append(methodPrintInt32)
                .append(methodPrintBool)
                .append(methodPrint)
                .append(methodInputInt32)
                .append(methodInputBool)
                .append(methodInputLine);
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
