package parserClasses;

import llvm.Generator;

import java.util.*;

public class Classe extends  Node{

    private String name;
    private String parentClasse;
    private ClassBody body;
    public Classe(int pColumn, int pLine, String pName, ClassBody body)
    {
        super(pColumn,pLine);
        name = pName;
        parentClasse = "Object";
        this.body=body;

    }

    public Classe(int pColumn, int pLine, String pName, String pParentClasse,  ClassBody body)
    {
        super(pColumn,pLine);
        name = pName;
        parentClasse = pParentClasse;
        this.body=body;
    }

    @Override
    public String toString(boolean checkerMode)
    {
        return  "Class(" + name + ", " + parentClasse + ", " + body.toString(checkerMode) + ")";
    }

    @Override
    public void toLlvm(Generator g) {
        g.declarationsBuilder.add(new StringBuilder(name));

        // Add the constructor to the methods of the class
        StringBuilder constructor = new StringBuilder();
        constructor.append("\ndefine %classe.").append(name).append("* @").append(name).append("-new() {\n");
        constructor.append("    %size = getelementptr  %classe.").append(name).append("* null, i32 1\n");
        constructor.append("    %sizeI = ptrtoint %classe.").append(name).append("* %size to i64\n");
        constructor.append("    %1 = call noalias i8* @malloc(i64 %sizeI)\n");
        constructor.append("    %self = bitcast i8* %1 to %classe.").append(name).append("*\n");
        constructor.append("    call void @").append(name).append("-new-init(%classe.").append(name).append("* %self)\n");
        constructor.append("    ret %classe.").append(name).append("* %self\n}\n\n");
        // Add the initializer
        constructor.append("define void @").append(name).append("-new-init(%classe.").append(name).append("* %this) {\n");
        g.counter = 1;

        //The class extends another class (not Object)
        if(parentClasse != null && !parentClasse.equals("Object")) {
            constructor.append("    %").append(g.counter).append(" = bitcast ").append("%classe.").append(name)
                    .append("* %this to %classe.").append(parentClasse).append("*\n").append("    call void @")
                    .append(parentClasse).append("-new-init(%classe.").append(parentClasse).append("* %")
                    .append(g.counter++).append(")\n");
        }

        constructor.append("    %").append(g.counter).append(" = getelementptr inbounds %classe.").append(name)
                .append("* %this, i32 0, i32 0\n").append("    store %table.").append(name).append("VTable* @")
                .append(name).append("VTableGlobal, %table.").append(name).append("VTable** %").append(g.counter++).append("\n");

        if(name.equals("Object"))
        {
            constructor.append("    ret void\n}\n\n");
            g.methodsBuilder.add(constructor);
            g.output.append(g.methodsBuilder.getLast());
            return;
        }
        Collection<Field> fields = g.c.allowedField.get(name).values();
        String init = null;
        for(Field f : fields) {
            Expressions exp = f.getExpression();
            int pos = 0;
            for(Object element : g.c.classFieldType.keySet().toArray()) {
                if(element.equals(f))
                    break;
                pos++;
            }
            if(exp != null) {
                exp.toLlvm(g);

                if(f.retType.equals("unit"))
                    init = "0";
                else {
                    init = exp.value;
                }
            }

            constructor.append("    %").append(g.counter).append(" = getelementptr inbounds %classe.").append(name)
                    .append("* %this, i32 0, i32 ").append(pos).append("\n").append("    store ")
                    .append(f.retType).append(" ").append(init).append(", ").append(f.retType).append("* %")
                    .append(g.counter++).append("\n");
        }

        constructor.append("    ret void\n}\n\n");

        g.methodsBuilder.add(constructor);
        g.output.append(g.methodsBuilder.getLast());
        g.counter = 0;
        if(!name.equals("IO")) {
            Collection<Method> methods = g.c.allowedMethods.get(name).values();
            methods.removeAll(g.c.allowedMethods.get(parentClasse).values());
            for (Method m : methods) {
                m.toLlvm(g);
                g.output.append(g.methodsBuilder.getLast());

            }
        }
    }


    public String getName()
    {
        return name;
    }

    public String getParentClasse()
    {
        return parentClasse;
    }

    public ClassBody getBody() {
        return body;
    }

}
