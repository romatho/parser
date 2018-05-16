package llvm;

import java.util.HashMap;

public class Generator {
    public int counter;
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
        }
        return "%class." + initType + "*";
    }
}
