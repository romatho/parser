package check;


import java.util.Map;


import parserClasses.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Checker {

    /*FIELD*/

    private String filename;
    public int toReturn =0;
    //map allowed class with their name
    private HashMap<String, Classe> allowedClasses;
    //map allowed method with their name and their class name
    private HashMap<String, HashMap<String, Method> > allowedMethods;
    //map allowed formals with their name , their associated name and their associated class name
    private HashMap<String, HashMap<String, HashMap<String, Formals> > > allowedFormals;
    //map allowed field with their name and their class name
    private HashMap<String, HashMap<String, Field> > allowedField;
    //the IO Classe
    private Classe IOClass;

    private HashMap<String, HashMap<String, String> > classFieldType;
    private HashMap<String, HashMap<String, String> > classMethodType;
    private HashMap<String, HashMap<String, ArrayList< Pair > > > classMethodFormalsType;
//    private HashMap<String, HashMap<String, HashMap<String, String> > > classMethodeFormalsType;

    /*CONSTRUCTOR*/

    private void buildIOClass()
    {
        ClassBody IoBodyClass = new ClassBody(0,0);

        // print(s:string): IO{}
        ParserArray<Formals> parsFormPrint= new ParserArray<>();
        parsFormPrint.add(new Formals(0,0, "s",new Types(0,0, "string")));
        //System.out.println(parsForm.get(0).toString(false));
        IoBodyClass.addMethod(new Method(0,0,"print", parsFormPrint, new Types(0,0,"IO") , new Blocks(0,0, new ParserArray<Expressions>())));
        // printBool(b:bool): IO{}
        ParserArray<Formals> parsFormPrintBool= new ParserArray<>();
        parsFormPrintBool.add(new Formals(0,0, "b",new Types(0,0, "bool")));
        IoBodyClass.addMethod(new Method(0,0,"printBool", parsFormPrintBool, new Types(0,0,"IO") , new Blocks(0,0, new ParserArray<Expressions>())));

        // printInt32(i:int32): IO{}
        ParserArray<Formals> parsFormPrintInt32= new ParserArray<>();
        parsFormPrintInt32.add(new Formals(0,0, "i",new Types(0,0, "int32")));
        IoBodyClass.addMethod(new Method(0,0,"printInt32", parsFormPrintInt32, new Types(0,0,"IO") , new Blocks(0,0, new ParserArray<Expressions>())));

        // inputLine(): string{}
        ParserArray<Formals> parsFormEmpty = new ParserArray<>();
        IoBodyClass.addMethod(new Method(0,0,"inputLine", parsFormEmpty, new Types(0,0,"string") , new Blocks(0,0, new ParserArray<Expressions>())));

        // inputBool(): bool{}
        IoBodyClass.addMethod(new Method(0,0,"inputBool", parsFormEmpty, new Types(0,0,"bool") , new Blocks(0,0, new ParserArray<Expressions>())));

        // inputInt32(): int32{}
        IoBodyClass.addMethod(new Method(0,0,"inputInt32", parsFormEmpty, new Types(0,0,"int32") , new Blocks(0,0, new ParserArray<Expressions>())));

        IOClass = new Classe(0, 0, "IO", "Object",  IoBodyClass);
    }

    public Checker(Program program, String filename)
    {
        this.filename=filename;
        toReturn = 0;
        allowedClasses = new HashMap<>();
        allowedMethods = new HashMap<>();
        allowedField = new HashMap<>();
        allowedFormals = new HashMap<>();
        classFieldType = new HashMap<>();
        classMethodType = new HashMap<>();
        classMethodFormalsType = new HashMap<>();
        buildIOClass();
        semanticCheck(program);
        // System.out.println(program.toString(false));
        //System.out.println(program.toString(true));

    }

    /*PRIVATE*/


    /*
     * Check if the string is a correct Type:
     * allowed type-identifier | "int32" | "bool" | "string" | "unit"
     */

    private Boolean allowedType(String typeToTest)
    {
        return (allowedClasses.containsKey(typeToTest) ||
                typeToTest.equals("IO") ||
                typeToTest.equals("Object") ||
                typeToTest.equals("int32") ||
                typeToTest.equals("bool") ||
                typeToTest.equals("string") ||
                typeToTest.equals("unit"));
    }

    /*PUBLIC*/

    public void semanticCheck(Program program)
    {
        //first pass
        checkClass(program.getClasses());
        //second pass
        for(HashMap.Entry<String, Classe> current: allowedClasses.entrySet()){
            checkMethod(current.getValue());
            checkField(current.getValue());
        }
        //displayHash();
        addInheritance();
        convertHashmap();
        //displayHash();
        //displayStringHash();
        //System.out.println(program.toString(false));
        //displayStringHash();
        getType(program);
        //System.out.println(program.toString(true));
    }

    /*
     * First Pass:
     *  - Check for class re-declaration
     *  - Check if 'Main' class is implemented
     *  - Check for cycle in the predecessors of a class
     *  - Check if class has a defined predecessor
     *  - Fill the element 'allowedClasses' that contains all the correct classes of the Program mapped by their name ( /!\Does not contain IO neither Object)
     */

    public void checkClass(ParserArray<Classe> sons)
    {
        /*
         * classesMap is a map containing all the Classes of the program
         * Creation of classesMap and check the re-definition of Classe
         */
        HashMap<String, Classe> classesMap = new HashMap<>();
        classesMap.put(IOClass.getName(), IOClass);

        for(Classe current: sons)
        {
            // re-definition if the class exist in the already created map or if the class name is "IO" or "Object"
            if(!classesMap.containsKey(current.getName()) &&
                    /*!current.getName().equals("IO") ||*/
                    !current.getName().equals("Object"))
                classesMap.put(current.getName(), current);
            else
            {
                System.err.println(filename + ":" + current.line + ":" +current.column + ": semantic error: re-definition of class '" + current.getName() + "'");
                toReturn =1;
            }
        }

        //check no definition of "Main"
        if(!classesMap.containsKey("Main"))
        {
            System.err.println(filename + ":0:0: semantic error: Main class not defined ");
            toReturn = 1;
        } else if (!(classesMap.get("Main").getParentClasse().equals("IO") ||
                classesMap.get("Main").getParentClasse().equals("Object")) )
        {
            System.err.println(filename + ":0:0: semantic error: main does not implement IO");
            toReturn = 1;
        }

        /*
         * For each Classe in classesMap, loop on predecessors to create a 'toAdd' map with all the predecessors of the current Classe
         * This 'toAdd' is then added to the map of classes 'allowedClasses'.
         */
        for(HashMap.Entry<String, Classe> entry : classesMap.entrySet())
        {
            Map<String, Classe> toAdd = new HashMap<>();
            String it = entry.getKey();
            /*
             * The loop stop if :
             *      The iterator is a undefined class (error)
             *      The iterator is a class that is already in the toReturn Map
             *      The iterator is a class that is already in the toAdd Map (Cycle in the predecessor list => error)
             *      The iterator is the class "IO" or "Object"
             * */
            while(  classesMap.containsKey(it) &&
                    !allowedClasses.containsKey(it) &&
                    !toAdd.containsKey(it) &&
                    /*!it.equals("IO") &&*/
                    !it.equals("Object"))
            {
                toAdd.put(it, classesMap.get(it));
                it = classesMap.get(it).getParentClasse();
            }
            if(toAdd.containsKey(it))
            {
                System.err.println(filename + ":" + classesMap.get(it).line + ":" + classesMap.get(it).column + ": semantic error: cycle in the predecessor: '" + it + "' become is own predecessor");
                toReturn = 1;
            }
            else if(!classesMap.containsKey(it) &&
                    /*!it.equals("IO") &&*/
                    !it.equals("Object"))
            {
                System.err.println(filename + ":" + classesMap.get(it).line + ":" + classesMap.get(it).column + ": semantic error: the parent class '" + it + "' is not defined");
                toReturn = 1;
            }
            else
                allowedClasses.putAll(toAdd);
        }
    }


    /*
     * Second Pass (about Method of class):
     *  - Check for re-declaration of method
     *  - Check if method has a valid expected return type (does not check the last element of the body, just the return type defined in the prototype of the method
     *  - Check if Main class implement a main method
     *  - Check if main method has no argument
     *  - Check if main method has a expected return type int32 (expected return type = field 'ReturnType' in the Method class)
     *  - Check if another class implement a main method TODO: est ce que c'est autorisé ou non ? j'ai fait la fonction pour trigger une erreur au pire on l'enlèvera
     *  - Check if the formals of the methods have a valid type
     */

    public void checkMethod(Classe classe) {

        /*
         * methodMap is a map containing all the Method of the class
         * Creation of methodMap and check the re-definition of Method
         * Check if the expected returnType is one of the following:
         *      correct type-identifier | "int32" | "bool" | "string" | "unit"
         */
        HashMap<String, HashMap <String, Formals> > formalMethodMap = new HashMap<>();
        HashMap<String, Method> methodMap = new HashMap<>();
        for (Method current : classe.getBody().getMyMethods())
        {
            if (methodMap.containsKey(current.getIdentifier()))
            {
                //erreur redéfinition d'une méthode
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: re-definition of class '" + current.getIdentifier() + "'");
                toReturn = 1;
            }
            else if (!allowedType(current.getReturnType()))
            {
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: the returned type '" + current.getReturnType() + "' of the method '" + current.getIdentifier() + "' does not exist");
                toReturn = 1;
            }
            else
            {
                /*
                 * Check formals type of the current method
                 */
                HashMap<String, Formals> currentFormal = new HashMap<>();
                if(!current.getFormals().isEmpty())
                    currentFormal = checkFormal(current);
                if(current.getFormals().size() == currentFormal.size())
                {
                    methodMap.put(current.getIdentifier(), current);
                    formalMethodMap.put(current.getIdentifier(), currentFormal);
                }
            }
        }

        /*
         * Check :
         * If Main class implements main method
         * If the main method has no argument
         * If the main method return a int32
         * If the class is not main, check if it implement a main method
         */
        if(classe.getName().equals("Main")) {
            if(!methodMap.containsKey("main")) {
                System.err.println(filename + ":0:0: semantic error: Main class does not have a method main");
            }
            else {
                Boolean mainCorrectyImplemented = true;
                if(!methodMap.get("main").getFormals().isEmpty()) {
                    System.err.println(filename + ":" + methodMap.get("main").line + ":" + methodMap.get("main").column + ": semantic error: the main method of the Main class has at least one formal and does need");
                    toReturn = 1;
                    mainCorrectyImplemented= false;
                }
                if(!methodMap.get("main").getReturnType().equals("int32")){
                    System.err.println(filename + ":" + methodMap.get("main").line + ":" + methodMap.get("main").column + ": semantic error: the main method of the Main class must return an int32");
                    toReturn = 1;
                    mainCorrectyImplemented = false;
                }
                if(!mainCorrectyImplemented)
                {
                    methodMap.remove("main");
                    formalMethodMap.remove("main");
                }
            }
        }
        else if(methodMap.containsKey("main"))
        {
            methodMap.remove("main");
            formalMethodMap.remove("main");
            System.out.println(filename + ":" + classe.line + ":" + classe.column + ": semantic error: '" + classe.getName() + "'  has a main method but only Main class is allowed to have a main method");
        }
        allowedMethods.put(classe.getName(), methodMap);
        allowedFormals.put(classe.getName(), formalMethodMap);
    }

    /*
     * Second Pass (about Formals of method):
     *  - Check for re-declaration of Formal
     *  - Check if field has a valid type
     */

    public HashMap<String, Formals> checkFormal(Method method) {

        /*
         * fieldMap is a map containing all the fields of the class
         * Creation of fieldMap and check the re-definition of Field
         */
        HashMap<String, Formals> formalMap = new HashMap<>();
        for (Formals current : method.getFormals()) {
            if (formalMap.containsKey(current.getIdentifier())) {
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: formal redefinition");
                toReturn = 1;
            }
            else if (!allowedType(current.getType())) {
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: the type '" + current.getType() + "' of the formal '" + current.getIdentifier() + "' is not defined");
                toReturn = 1;
            }
            else
                formalMap.put(current.getIdentifier(), current);
        }
        return formalMap;
    }

    /*
     * Second Pass (about Field of class):
     *  - Check for re-declaration of field
     *  - Check if field has a valid type
     *  - Check if field is named 'self'
     *
     * */

    public void checkField(Classe classe) {

        /*
         * fieldMap is a map containing all the fields of the class
         * Creation of fieldMap and check the re-definition of Field
         */
        HashMap<String, Field> fieldMap = new HashMap<>();
        for (Field current : classe.getBody().getMyFields()) {
            if (fieldMap.containsKey(current.getIdentifier())) {
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: field redefinition");
                toReturn = 1;
            }
            else if (!allowedType(current.getType())) {
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: the type '" + current.getType() + "' of the field '" + current.getIdentifier() + "' is not defined");
                toReturn = 1;
            }
            else if(current.getIdentifier().equals("self"))
            {
                System.err.println(filename + ":" + current.line + ":" + current.column + ": semantic error: in " + classe.getName() + " a field cannot be named 'self'");
                toReturn = 1;
            }
            else
                fieldMap.put(current.getIdentifier(), current);
        }
        allowedField.put(classe.getName(), fieldMap);
    }


    public void displayHash() {
        System.out.println();
        System.out.println("***affichage hashtable Classe***");
        System.out.println();
        for (HashMap.Entry<String, Classe> entry : allowedClasses.entrySet()) {
            System.out.print(" nom classe : ");
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().toString(false));
        }
        System.out.println();
        System.out.println("***affichage hashtable Method***");
        System.out.println();
        for (HashMap.Entry<String, HashMap<String, Method> > entry : allowedMethods.entrySet()) {
            System.out.print("nom classe : ");
            System.out.println(entry.getKey());
            for (HashMap.Entry<String, Method> entrySec : entry.getValue().entrySet()) {
                System.out.print("nom methode : ");
                System.out.print(entrySec.getKey());
                System.out.print(" valeur methode : ");
                System.out.println(entrySec.getValue().toString(false));
            }
        }
        System.out.println();
        System.out.println("***affichage hashtable Field***");
        System.out.println();
        for (HashMap.Entry<String, HashMap<String, Field> > entry : allowedField.entrySet()) {
            System.out.print("nom classe : ");
            System.out.println(entry.getKey());
            for (HashMap.Entry<String, Field> entrySec : entry.getValue().entrySet()) {
                System.out.print("nom field : ");
                System.out.print(entrySec.getKey());
                System.out.print(" valeur field : ");
                System.out.println(entrySec.getValue().toString(false));
            }
        }
    }

    public void displayStringHash() {

        System.out.println();
        System.out.println("***display String Hash Debut***");
        System.out.println("***affichage hashtable field***");
        System.out.println();
        for (HashMap.Entry<String, HashMap<String, String> > classEntry : classFieldType.entrySet()) {
            System.out.print("nom classe : ");
            System.out.println(classEntry.getKey());
            if(classFieldType.containsKey(classEntry.getKey()))
                for(HashMap.Entry<String, String> fieldEntry : classFieldType.get(classEntry.getKey()).entrySet())
                {
                    System.out.print("\t nom field : ");
                    System.out.print(fieldEntry.getKey());
                    System.out.println(" return type: " + fieldEntry.getValue());
                }

        }

        System.out.println();
        System.out.println("***affichage hashtable method***");
        System.out.println();
        for (HashMap.Entry<String, HashMap<String, String> > classEntry : classMethodType.entrySet()) {
            System.out.print("nom classe : ");
            System.out.println(classEntry.getKey());
            for(HashMap.Entry<String, String> methodEntry : classMethodType.get(classEntry.getKey()).entrySet())
            {
                System.out.print("\t nom method : ");
                System.out.print(methodEntry.getKey());
                System.out.println(" return type: " + methodEntry.getValue());
            }

        }

        System.out.println();
        System.out.println("***affichage hashtable method formal***");
        System.out.println();
        for (HashMap.Entry<String, HashMap<String, ArrayList < Pair > > > classEntry : classMethodFormalsType.entrySet()) {
            System.out.print("nom classe : ");
            System.out.println(classEntry.getKey());

            for (HashMap.Entry<String, ArrayList < Pair > > methodEntry : classEntry.getValue().entrySet()) {
                System.out.print("\t nom methode : ");
                System.out.println(methodEntry.getKey());
                for (Pair formalEntry : methodEntry.getValue()) {
                    System.out.print("\t \t ");
                    System.out.print("nom formal : ");
                    System.out.print(formalEntry.getKey());
                    System.out.print(" return type: ");
                    System.out.println(formalEntry.getValue());
                }
            }
        }
        System.out.println("***display String Hash Fin***");
    }





    public void addInheritance()
    {
        ArrayList<String> inheritanceNotDone= new ArrayList<>();
        for(HashMap.Entry<String, Classe> entry : allowedClasses.entrySet())
        {
            if(/*!entry.getValue().getParentClasse().equals("IO") &&*/ !entry.getValue().getParentClasse().equals("Object" ))
                inheritanceNotDone.add(entry.getKey());
        }
        while(inheritanceNotDone.size() !=0)
        {
            for(HashMap.Entry<String, Classe> entry : allowedClasses.entrySet())
            {
                //then the parent class contain all the method and field of his predecessor
                if(!inheritanceNotDone.contains(entry.getValue().getParentClasse()) )
                {
                    //check in all the method of the parent
                    if(allowedMethods.containsKey(entry.getValue().getParentClasse()))
                    {
                        for(HashMap.Entry<String, Method> method: allowedMethods.get(entry.getValue().getParentClasse()).entrySet() )
                        {
                            if(allowedMethods.get(entry.getKey()).containsKey(method.getKey()))
                            {
                                Method parentMethod = method.getValue();
                                Method currentClassMethod = allowedMethods.get(entry.getKey()).get(method.getValue().getIdentifier());
                                if(!checkMethodPrototypeEquality(parentMethod, currentClassMethod))
                                {
                                    System.err.println(filename + ":" + currentClassMethod.line + ":" + currentClassMethod.column + ": semantic error: Wrong re-definition of the parent Method " + parentMethod.getIdentifier());
                                    toReturn = 1;
                                }
                            }
                            else
                            {
                                allowedMethods.get(entry.getKey()).put(method.getKey(), method.getValue());
                                HashMap<String, Formals> toAdd = allowedFormals.get(entry.getValue().getParentClasse()).get(method.getValue().getIdentifier());
                                allowedFormals.get(entry.getKey()).put(method.getKey(), toAdd);
                            }
                        }
                    }
                    //check in all the Field of the parent
                    if(allowedField.containsKey(entry.getValue().getParentClasse()))
                    {
                        for(HashMap.Entry<String, Field> field: allowedField.get(entry.getValue().getParentClasse()).entrySet())
                        {
                            if(allowedField.get(entry.getKey()).containsKey(field.getKey()))
                            {
                                System.err.println(filename + ":" + field.getValue().line + ":" + field.getValue().column + ": semantic error: re-definition of the parent Field " + field.getKey());
                                toReturn = 1;
                            }
                            else
                            {
                                allowedField.get(entry.getKey()).put(field.getKey(), field.getValue());
                            }
                        }
                    }
                    //remove the element for inheritance list
                    inheritanceNotDone.remove(entry.getKey());
                }
            }
        }
    }

    private Boolean checkMethodPrototypeEquality(Method a, Method b)
    {
        if(a.getReturnType().equals(b.getReturnType()) &&
                a.getFormals().size() == b.getFormals().size())
        {
            for(int i = 0; i< a.getFormals().size(); i++)
            {
                if(! a.getFormals().get(i).getType().equals(b.getFormals().get(i).getType()))
                    return false;
            }
            return true;
        }
        return false;
    }


    //convert hashmap of object into hashmap of String
    private void convertHashmap()
    {
        for(HashMap.Entry<String, Classe> classEntry: allowedClasses.entrySet()) {
            HashMap<String, String> methoConvert = new HashMap<>();
            HashMap<String, ArrayList< Pair > > formalConvert = new HashMap<>();
            HashMap<String, HashMap<String, Formals>> allowedFormalsMethod = allowedFormals.get(classEntry.getKey());
            if(allowedMethods.containsKey(classEntry.getKey())){
                for (HashMap.Entry<String, Method> methodEntry : allowedMethods.get(classEntry.getKey()).entrySet()) {
                    methoConvert.put(methodEntry.getKey(), methodEntry.getValue().getReturnType());

                    ArrayList< Pair > methodFormalConvert = new ArrayList<>();
                    if(methodEntry.getValue().getFormals().size() >0 )
                        for (Formals formal : methodEntry.getValue().getFormals())
                            methodFormalConvert.add(new Pair(formal.getIdentifier(), formal.getType()));
                    formalConvert.put(methodEntry.getKey(), methodFormalConvert);


                    /*
                    HashMap<String, String> methodeFormalConvert = new HashMap<>();
                    if(allowedFormalsMethod.containsKey(methodEntry.getKey())){
                        for (HashMap.Entry<String, Formals> formalEntry : allowedFormalsMethod.get(methodEntry.getKey()).entrySet())
                            methodeFormalConvert.put(formalEntry.getKey(), formalEntry.getValue().getType());
                        formalConvert.put(methodEntry.getKey(), methodeFormalConvert);
                    }*/

                }
                classMethodType.put(classEntry.getKey(), methoConvert);
                classMethodFormalsType.put(classEntry.getKey(), formalConvert);
            }
            /**/
            HashMap<String, String> fieldConvert = new HashMap<>();
            if(allowedField.containsKey(classEntry.getKey()))
            {
                for (HashMap.Entry<String, Field> fieldEntry : allowedField.get(classEntry.getKey()).entrySet())
                    fieldConvert.put(fieldEntry.getKey(), fieldEntry.getValue().getType());
                classFieldType.put(classEntry.getKey(), fieldConvert);
            }
        }
    }

    void getType(Program program)
    {
        for(HashMap.Entry<String, HashMap<String, Method> > entryClass : allowedMethods.entrySet())
        {
            //check if the class is not IO
            if(program.getClasses().contains(allowedClasses.get(entryClass.getKey()))) {
                for (HashMap.Entry<String, Method> entryMethod : allowedMethods.get(entryClass.getKey()).entrySet()) {
                    //check if the method is not a parent method : To avoid multiple 'getType' on the same method
                    if (allowedClasses.get(entryClass.getKey()).getBody().getMyMethods().contains(entryMethod.getValue())) {
                        entryMethod.getValue().getType(classFieldType, classMethodType, classMethodFormalsType, entryClass.getKey(), this.filename, this);
                    }
                }
                for (HashMap.Entry<String, Field> entryField: allowedField.get(entryClass.getKey()).entrySet()) {
                    //check if the field is not a parent method : To avoid multiple 'getType' on the same method
                    if (allowedClasses.get(entryClass.getKey()).getBody().getMyFields().contains(entryField.getValue()) && entryField.getValue().getExpression() != null ) {
                        entryField.getValue().getExpression().getType(classFieldType, classMethodType, classMethodFormalsType,  new HashMap<String,String>(), entryClass.getKey(), this.filename, null,this);
                    }
                }
            }
        }
    }
}
