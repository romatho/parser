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
    public int stringCounter=1;
    public int whileCounter=0;
    public Checker c;

    public HashMap<String, String> vars;
    public StringBuilder builder;
    public StringBuilder stringBuilder;
    public HashMap<String, StringHandler> strings;
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
        methodsBuilder = new LinkedList<>();
        strings= new LinkedHashMap<>();
        declarationsBuilder= new LinkedList<>();
        methodsBuilder= new LinkedList<>();
        createLLVM();
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
        String targetTriple = "target triple = \"x86_64";
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

        StringBuilder classObject = new StringBuilder("%classe." + current.getName() + " = type{ %table." + current.getName() + "VTable*}");
        StringBuilder vTableString = new StringBuilder("%table." + current.getName() + "VTable = type { ");
        StringBuilder vTableGlobalString = new StringBuilder("@" + current.getName() + "VTableGlobal = internal global %table." + current.getName() + "VTable { ");
        if(current.getName().equals("Object"))
        {
            String comma = "";
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
                vTableGlobalString.append(toAdd + " @" + current.getName()+"-" + method.getIdentifier());
            }
        }
        else {
            String comma = "";

            for (HashMap.Entry<String, Method> method : this.c.allowedMethods.get(current.getName()).entrySet()) {

                StringBuilder toAdd = new StringBuilder();
                toAdd.append(comma + typeConversion(method.getValue().getReturnType()) + " (");
                //add the class itself as formal
                toAdd.append("%classe." + current.getName() + "*");
                //add formals
                for (Formals formal : method.getValue().getFormals())
                    toAdd.append(", " + typeConversion(formal.getType()));
                toAdd.append(")*"); //bitcast
                StringBuilder toAddGlobal = new StringBuilder();
                StringBuilder toAddGlobal2 = new StringBuilder();
                if(!AncestorOwnerMethod(current, method.getKey()).equals(current.getName()))
                {
                    toAddGlobal2.append(" to " + toAdd.toString().replaceFirst(comma," ") + ")");
                    toAddGlobal.append("bitcast (" + typeConversion(method.getValue().getReturnType()) + " (%classe." +  AncestorOwnerMethod(current, method.getKey())+"*");
                    for (Formals formal : method.getValue().getFormals())
                        toAddGlobal.append(", " + typeConversion(formal.getType()));
                    toAddGlobal.append(")*");
                    comma = ",";
                    vTableString.append(toAdd);
                    vTableGlobalString.append(toAdd).append(toAddGlobal).append(" @" + AncestorOwnerMethod(current, method.getKey()) + "-" + method.getValue().getIdentifier());
                    vTableGlobalString.append(toAddGlobal2);
                }

                else{
                    comma = ",";
                    vTableString.append(toAdd);
                    vTableGlobalString.append(toAdd).append(toAddGlobal).append(" @" + current.getName() + "-" + method.getValue().getIdentifier());
                    vTableGlobalString.append(toAddGlobal2);
                }
            }
        }
        return classObject.append("\n" + vTableString.append( "}\n" + vTableGlobalString +  "}\n"));
    }

    //add the stdin from c
    String addStdIn()
    {
        return "%file = type { i32, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, %marker*, %file*, i32, i32, i64, i16, i8, [1 x i8], i8*, i64, i8*, i8*, i8*, i8*, i64, i32, [20 x i8] }" +
                "\n %marker = type { %marker*, %file*, i32 }";
    }

    StringBuilder automaticIOCreation()
    {
        // The implementaiton is done thankss to the definition of the class in the VSOP manual

        StringBuilder toReturn = new StringBuilder();

        //printf
        String methodPrintf = "declare i32 @printf(i8*, ...)\n declare i32 @scanf(i8*, ...)\n declare i64 @getline(i8**, i64*, %file*)\n declare i8* @malloc(i64)\n" +
                "\n" +
                "declare i32 @strcmp(i8*, i8*)";

        //printInt32 method
        String methodPrintInt32 = "define %classe.IO* @IO-printInt32(%classe.IO* %this, i32 %myInt32){\n" +
                "entry:\n"+
                "%0 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @formatInt , i32 0, i32 0), i32 %myInt32)\n" +
                "\tret %classe.IO* %this\n" +
                "}\n";


        //printBool method
        String methodPrintBool = "define %classe.IO* @IO-printBool(%classe.IO* %this, i1 %myBool){\n" +
                "entry:\n"+
                "\t%0 = icmp eq i1 %myBool, 0\n"+
                "\tbr i1 %0, label %labelFalse, label %labelTrue \n"+
                "labelFalse:\n"+
                "\t%1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @false , i32 0, i32 0))\n" +
                "\tret %classe.IO* %this\n" +
                "labelTrue:\n"+
                "%2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @true , i32 0, i32 0))\n" +
                "\tret %classe.IO* %this\n" +
                "}\n";


        //print method
        String methodPrint = "define %classe.IO* @IO-print(%classe.IO* %this, i8* %myString){\n" +
                "entry:\n" +
                "\t%0 = call i32(i8*,...) @printf(i8* %myString)\n" +
                "\tret %classe.IO* %this\n" +
                "}\n";


        //
        String methodInputInt32 = "define i32 @IO-inputInt32(%classe.IO* %this){\n" +
                "entry:\n" +
                "\t%0 = alloca %classe.IO*\n" +
                "\t%n = alloca i32\n" +
                "\tstore %classe.IO* %this, %classe.IO** %0\n" +
                "\t%1 = load  %classe.IO*, %classe.IO** %0\n" +
                "\t%2 = call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8],[3 x i8]* @formatInt, i32 0, i32 0), i32* %n)\n" +
                "\t%unused= call i8* @llvmGetLine()\n" +
                "\t%3= load  i32,i32* %n\n" +
                "\tret i32 %3\n" +
                "}\n";

        //
        String methodInputBool = "define i1 @IO-inputBool(%classe.IO* %this){\n"+
                "entry:\n" +
                "\t%0=  alloca %classe.IO*\n" +
                "\tstore %classe.IO* %this, %classe.IO** %0\n" +
                "\t%1 = load  %classe.IO*,%classe.IO** %0\n" +
                "\t%2 = call i8* @llvmGetLine()\n" +
                "\t%3 = call i32 @strcmp(i8* %2, i8*getelementptr inbounds ([6 x i8],[6 x i8]* @truecmp, i32 0, i32 0))\n" +
                "\t%4 = icmp eq i32 %3, 0\n" +
                "\tbr i1 %4, label %then, label %else\n" +
                "then:\n" +
                "\tbr label %end\n" +
                "else:\n" +
                "\tbr label %end\n" +
                "end:\n" +
                "\t%5 = phi i1 [true, %then], [false, %else]\n" +
                "\tret i1 %4"+
                "}\n";

        //
        String methodInputLine = "define i8* @IO-inputLine(%classe.IO* %this){\n" +
                "entry:\n" +
                "\t%0 = call i8* @llvmGetLine()" +
                "\tret i8* %0\n" +
                "}\n";

        String methodgetLineC = "define i8* @llvmGetLine(){\n" +
                "entry:\n" +
                "\t%0 = alloca i8*\n" +
                "\t%line = alloca i8*\n" +
                "\t%len = alloca i64\n" +
                "\t%nread = alloca i64\n" +
                "\tstore i8* null, i8** %line\n" +
                "\tstore i64 0, i64* %len\n" +
                "\tbr label %1\n" +
                "\t%2 = load %file*,%file** @stdin\n" +
                "\t%3 = call i64 @getline(i8** %line, i64* %len, %file* %2)\n" +
                "\tstore i64 %3, i64* %nread\n" +
                "\t%4 = icmp ne i64 %3, -1\n" +
                "\tbr i1 %4, label %5, label %7\n" +
                "\t%6 = load i8*,i8** %line\n" +
                "\tstore i8* %6, i8** %0\n" +
                "\tbr label %9\n" +
                "\t%8 = load i8*, i8** %line\n" +
                "\tstore i8* %8, i8** %0\n" +
                "\tbr label %9\n" +
                "\t%10 = load i8*, i8** %0\n" +
                "\tret i8* %10\n" +
                "}\n";
        toReturn.append(methodPrintf).append(methodPrintInt32)
                .append(methodPrintBool)
                .append(methodPrint)
                .append(methodInputInt32)
                .append(methodInputBool)
                .append(methodInputLine)
                .append(methodgetLineC);
        return toReturn;
    }

    String generateEntrypoint()
    {
        return "define i32 @main(){\n" +
                "%1 = call %classe.Main* @Main-new()\n" +
                "%2 = call i32 @Main-main(%classe.Main* %1)\n" +
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
        for(HashMap.Entry<String,StringHandler> string:strings.entrySet())
            output.append(string).append("\n");
        //TODO : add toLLVM()
    }


    public String AncestorOwnerMethod(Classe child, String methodName)
    {
        if(child.getParentClasse().equals("Object"))
            return child.getName();
       if(this.c.allowedMethods
               .get(child.getParentClasse())
               .containsKey(methodName))
           return AncestorOwnerMethod(this.c.allowedClasses.get(child.getParentClasse()), methodName );
       return child.getName();
    }

}
