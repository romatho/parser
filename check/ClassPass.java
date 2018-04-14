package check;

import parserClasses.Classe;

import java.util.HashMap;
import java.util.Map;

public class ClassPass {

    private int nbError;

    public ClassPass()
    {
        nbError = 0;
    }

    public HashMap<String, Classe> checkClass(Classe[] sons)
    {
        /*
         * ClassesMap is a map containing all the Classes of the program
         */
        HashMap<String, Classe> classesMap = new HashMap<>();

        /*
         * Creation of ClassesMap and check the re-definition of Classe
         */
        for(Classe current: sons)
        {
            // re-definition if the class exist in the already created map or if the class name is "IO" or "Object"
            if(!classesMap.containsKey(current.getName()) &&
                    !current.getName().equals("IO") ||
                    !current.getName().equals("Object"))
                classesMap.put(current.getName(), current);
            else
            {
                //erreur redéfinition d'une classe (on peut spliter via un check pour séparer l'erreur "deux classes avec le même nom" et l'erreur " Override IO ou Object
                System.out.println("erreur redefinition");
                nbError++;
            }
        }

        //check no definition of "Main"
        if(!classesMap.containsKey("Main"))
        {
            System.out.println("erreur main non défini");
            nbError++;
        }

        /*
         * For each Classe in classesMap, loop on predecessors to create a 'toAdd' map with all the predecessors of the current Classe
         * This 'toAdd' is then added to the map of classes 'toReturn'.
         */
        HashMap<String, Classe> toReturn = new HashMap<>();

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
                    !toReturn.containsKey(it) &&
                    !toAdd.containsKey(it) &&
                    !it.equals("IO") &&
                    !it.equals("Object"))
            {
                toAdd.put(it, classesMap.get(it));
                it = classesMap.get(it).getParentClasse();
            }
            if(toAdd.containsKey(it))
            {
                System.out.println("erreur cycle dans ancetre");
                nbError++;
            }
            else if(!classesMap.containsKey(it))
            {
                System.out.println("erreur classe parent non définie");
                nbError++;
            }
            toReturn.putAll(toAdd);
        }
        return toReturn;
    }
}
