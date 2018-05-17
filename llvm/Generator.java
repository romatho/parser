package llvm;

import check.Checker;
import parserClasses.Classe;
import parserClasses.Formals;
import parserClasses.Method;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Generator {
    public int counter;
    public int ifCounter=0;
    public int stringCounter=0;
    public int whileCounter=0;
    public Checker c;

    public HashMap<String, String> vars;
    public StringBuilder builder;
    public StringBuilder stringBuilder;
    public LinkedHashMap<String, StringHandler> strings;
    public LinkedList<StringBuilder> declarationsBuilder;
    public LinkedList<StringBuilder> methodsBuilder;

    public StringBuilder output;

    public Generator(Checker c)
    {
        this.c = c;
        counter = 1;
        ifCounter = 1;
        builder = new StringBuilder();
        output = new StringBuilder();
        createLLVM();
        methodsBuilder = new LinkedList<>();
    }


    /**
     * Converts a VSOP type to a LLVM type
     * @param initType string containing the VSOP type
     * @return the corresponding LLVM type in a string
     */
    public String typeConversion(String initType) {
        if(initType == null)
            return null;
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
        String targetTriple = "target triple = \"" + System.getProperty("os.arch");
        String lowerOsName = System.getProperty("os.name").toLowerCase();
        if (lowerOsName.contains("mac"))
            targetTriple += "-apple-macosx";
        else if (lowerOsName.contains("win"))
            targetTriple += "-pc-windows";
        else
            targetTriple += "-pc-linux-gnu";
        return targetTriple + "\"\n";
    }

    private StringBuilder vTableToString(Classe current)
    {
        String comma = "";
        StringBuilder classObject = new StringBuilder("%classe." + current.getName() + " = type{ %table." + current.getName() + "VTable}");
        StringBuilder vTableString = new StringBuilder("%table." + current.getName() + "VTable = type { ");
        StringBuilder vTableGlobalString = new StringBuilder("@" + current.getName() + "VTableGlobal = internal global %table." + current.getName() + "VTable { ");

        for(Method method : current.getBody().getMyMethods())
        {
            StringBuilder toAdd = new StringBuilder();
            toAdd.append(comma + typeConversion(method.getReturnType()) + " (");
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
        return classObject.append("\n" + vTableString.append( "}\n" + vTableGlobalString +  "}\n"));
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
        String methodPrintf = "declare i32 @printf(i8*, ...)\n declare i32 @scanf(i8*, ...)\n";

        //printInt32 method
        String methodPrintInt32 = "define %classe.IO* @IOprintInt32(%classe.IO* %this, i32 %myInt32){\n" +
                "entry:\n"+
                "%1 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @formatInt , i32 0, i32 0), i32 %myInt32)\n" +
                "\tret %class.IO* %this\n" +
                "}\n";


        //printBool method
        String methodPrintBool = "define %classe.IO* @IOprintBool(%classe.IO* %this, i1 %myBool){\n" +
                "entry:\n"+
                "\t%1 = icmp eq i1 %myBool, 0\n"+
                "\tbr = i1 %1 label %labelFalse, label %labelTrue \n"+
                "labelFalse:\n"+
                "%2 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @false , i32 0, i32 0))\n" +
                "\tret %class.IO* %this" +
                "labelTrue:\n"+
                "%3 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @true , i32 0, i32 0))\n" +
                "\tret %class.IO* %this\n" +
                "}\n";


        //print method
        String methodPrint = "define %classe.IO* @IOprint(%classe.IO* %this, i8 %myString){\n" +
                "entry:\n" +
                "\t%1 = call i32(i8*,...)* @printf(i8 %myString)\n" +
                "\tret classe.IO* %this\n" +
                "}\n";


        //
        String methodInputInt32 = "define i32 @IOinputInt32(%classe.IO* %this, i8 %myString){\n" +
                "entry:\n" +
                "\t%1 = alloca %classe.IO*" +
                "\t%n = alloca i32" +
                "\tstore %classe.IO* %this, %classe.IO** %1" +
                "\t%2 = load %classe.IO** %1" +
                "\t%3 = call i32 (i8*, ...)* @scanf(i8* getelemntptr inbounds ([3 x i8]* @formatInt, i32 0, i32 0), i32* %n)\n" +
                "\t%unused= call i8* @llvmGetLine()" +
                "\t%4= load i32* %n)" +
                "\tret i32* %4" +
                "}\n";

        //
        String methodInputBool = "define i1 @IOinputBool(%classe.IO* %this){\n"+
                "entry:\n" +
                "\t%1 alloc %classe.IO*\n" +
                "\tstore %classe.IO* %this, %classe.IO** %1\n" +
                "\t%2 = load %classe.IO** %1\n" +
                "\t%3 = call i8* @llvmGetLine()\n" +
                "\t%4 = call i32* strcmp(i8* %3, i8*getelementptr inbound ([6 x i8]* @truecmp, i32 0, i32 0))\n" +
                "\t%5 = icmp eq i32 %4, 0\n" +
                "\tbr i1 %5, label %then, label %else\n" +
                "then:\n" +
                "\tbr label %end\n" +
                "else:\n" +
                "\tbr label %end\n" +
                "end:\n" +
                "\t%6 = phi i1 [true, %then], [false, %else]\n" +
                "\tret i1 %5"+
                "}\n";

        //
        String methodInputLine = "define i8* @IOinputLine(){\n" +
                "entry:\n" +
                "\t%1 = call i8* @llvmGetLine()" +
                "\tret i8* %1\n" +
                "}\n";

        String methodgetLineC = "define i* llvmGetLine(){" +
                "entry:\n" +
                "\t%1 = alloca i8*" +
                "\t%line = alloca i8*" +
                "\t%len = alloca i64" +
                "\t%nread = alloca i64" +
                "\tstore i8* null, i8** %line" +
                "\tstore i64 0, i64* %len" +
                "\tbr label %2" +
                "\t%3 = load %struct._IO_FILE** @stdin" +
                "\t%4 = call i64 @getline(i8** %line, i64* %len, %struct._IO_FILE* %3)" +
                "\tstore i64 %4, i64* %nread" +
                "\t%5 = icmp ne i64 %4, -1" +
                "\tbr i1 %5, label %6, label %" +
                "\t%7 = load i8** %line" +
                "\tstore i8* %7, i8** %1" +
                "\tbr label %10" +
                "\t%9 = load i8** %line" +
                "\tstore i8* %9, i8** %1" +
                "\tbr label %10" +
                "\t%11 = load i8** %1" +
                "\tret i8* %11" +
                "}";

        return toReturn.append(methodPrintInt32)
                .append(methodPrintBool)
                .append(methodPrint)
                .append(methodInputInt32)
                .append(methodInputBool)
                .append(methodInputLine)
                .append(methodgetLineC);
    }

    String generateEntrypoint()
    {
        return "define i32 @main(){\n" +
                "%1 = call %classe.Main* @Main-new()\n" +
                "%2 = call i32 @Mainmain(%classe.Main* %1)\n" +
                "ret i32 %2 \n" +
                "}";
    }

    private String addConstant() {
        return "@true = constant [5 x i8] c\"true\\00\"\n" +
                "@false = constant [6 x i8] c\"false\\00\"\n" +
                "@formatInt = constant [3 x i8] c\"%d\\00\"\n" +
                "@formatString = constant [3 x i8] c\"%s\\00\"\n" +
                "@stdin = external global %file*\n" +
                "@truecmp = constant [6 x i8] c\"true\\0A\\00\"\n" +
                "@.strempty = constant [1 x i8] c\"\\00\"\n";
    }

    public void createLLVM()
    {
        output = new StringBuilder();
        //target triple for compilation with clang
        output.append(getTargetTriple());
        //vTable for IO and Object
        output.append(vTableToString(c.objectClass) + "\n");
        output.append(vTableToString(c.IOClass) + "\n");
        //vTable for IO and Object
        for(Classe current : c.p.getClasses())
            output.append(vTableToString(current) + "\n");
        //create constant
        output.append(addStdIn() + "\n");
        output.append(addConstant() + "\n");

        //generate the method for IO class
        output.append(automaticIOCreation() + "\n");
        //add entry point
        output.append(generateEntrypoint());

        c.p.toLlvm(this);
        output.append(c.p.output);

        //TODO : add toLLVM()
    }


}
