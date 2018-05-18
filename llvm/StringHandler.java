package llvm;

public class StringHandler {
    public String value;
    public String identifier;
    public String size;

    public String toString() {

        if (size != null) {
            return identifier + " = constant " + size + " " + value;
        }

        return identifier + " = external global " + value;
    }
}
