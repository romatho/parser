package check;

import javafx.util.Pair;
import parserClasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Checker {

    /*FIELD*/


    private int nbError;
    //map allowed class with their name
    private HashMap<String, Classe> allowedClasses;
    //map allowed method with their name and their class name
    private HashMap<String, HashMap<String, Method> > allowedMethods;
    //map allowed formals with their name , their associated name and their associated class name
    private HashMap<String, HashMap<String, HashMap<String, Formals> > > allowedFormals;
    //map allowed field with their name and their class name
    private HashMap<String, HashMap<String, Field> > allowedField;



    private HashMap<String, HashMap<String, String> > classFieldType;
    private HashMap<String, HashMap<String, String> > classMethodType;
    private HashMap<String, HashMap<String, ArrayList< Pair<String, String> > > > classMethodFormalsType;

    /*CONSTRUCTOR*/


    public Checker(Program program)
    {
        nbError = 0;
        allowedClasses = new HashMap<>();
        allowedMethods = new HashMap<>();
        allowedField = new HashMap<>();
        allowedFormals = new HashMap<>();
        System.out.println("Checker initialized successfully.");
        semanticCheck(program);
        displayHash();
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
        addInheritance();


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

        for(Classe current: sons)
        {
            // re-definition if the class exist in the already created map or if the class name is "IO" or "Object"
            if(!classesMap.containsKey(current.getName()) &&
                    !current.getName().equals("IO") ||
                    !current.getName().equals("Object"))
                classesMap.put(current.getName(), current);
            else
            {
                System.out.println("Error: re-definition of class '" + current.getName() + "'");
                nbError++;
            }
        }

        //check no definition of "Main"
        if(!classesMap.containsKey("Main"))
        {
            System.out.println("Error: Main class not defined ");
            nbError++;
        } else if (!classesMap.get("Main").getParentClasse().equals("IO"))
        {
            System.out.println("Error: main does not implement IO");
            nbError++;
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
                    !it.equals("IO") &&
                    !it.equals("Object"))
            {
                toAdd.put(it, classesMap.get(it));
                it = classesMap.get(it).getParentClasse();
            }
            if(toAdd.containsKey(it))
            {
                System.out.println("Error: cycle in the predecessor: '" + it + "' become is own predecessor");
                nbError++;
            }
            else if(!classesMap.containsKey(it) &&
                    !it.equals("IO") &&
                    !it.equals("Object"))
            {
                System.out.println("Error: the parent class '" + it + "' is not defined");
                nbError++;
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
                System.out.println("Error: re-definition of class '" + current.getIdentifier() + "'");
                nbError++;
            }
            else if (!allowedType(current.getReturnType()))
            {
                System.out.println("Error: the returned type '" + current.getReturnType() + "' of the method '" + current.getIdentifier() + "' does not exist");
                nbError++;
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
                System.out.println("Error: Main class does not have a method main");
            }
            else {
                Boolean mainCorrectyIplemented = true;
                if(!methodMap.get("main").getFormals().isEmpty()) {
                    System.out.println("Error: the main method of the Main class has at least one formal");
                    nbError++;
                    mainCorrectyIplemented= false;
                }
                if(!methodMap.get("main").getReturnType().equals("int32")){
                    System.out.println("Error: the main method of the Main class must return an int32");
                    nbError++;
                    mainCorrectyIplemented = false;
                }
                if(!mainCorrectyIplemented)
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
            System.out.println("Error: '" + classe.getName() + "'  has a main method but only Main claas is allowed to have a main method");
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
                System.out.println("Error: formal redefinition");
                nbError++;
            }
            else if (!allowedType(current.getType())) {
                System.out.println("Error: the type '" + current.getType() + "' of the formal '" + current.getIdentifier() + "' is not defined");
                nbError++;
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
                System.out.println("Error: field redefinition");
                nbError++;
            }
            else if (!allowedType(current.getType())) {
                System.out.println("Error: the type '" + current.getType() + "' of the field '" + current.getIdentifier() + "' is not defined");
                nbError++;
            }
            else if(current.getIdentifier().equals("self"))
            {
                System.out.println("Error: in " + classe.getName() + " a field cannot be named 'self'");
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
            System.out.print("nom classe : ");
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().toString());
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
                System.out.println(entrySec.getValue().toString());
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
                System.out.println(entrySec.getValue().toString());
            }
        }
    }
    public void addInheritance()
    {
        ArrayList<String> inheritanceNotDone();
        for(HashMap.Entry<String, Classe> entry : allowedClasses.entrySet())
        {
            if(!(entry.getValue().getParentClasse() == "IO") && !(entry.getValue().getParentClasse() == "Object" ))
                inheritanceNotDone.add(entry.getKey());
        }
        while(inheritanceNotDone.size() !=0)
        {
            for(HashMap.Entry<String, Classe> entry : allowedClasses.entrySet())
            {
                //then the parent class contain all the method and field of his predecessor
                if(!inheritanceNotDone.contains(entry.getValue().getParentClasse()) )
                {
                    for(HashMap.Entry<String, Method> methodList : allowedMethods.entrySet())
                }

            }
        }
    }
}
