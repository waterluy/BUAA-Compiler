package mid.midcodes;

public class FunctionCall implements MidCode {
    private final String logo;
    private final String name;

    public FunctionCall(String n) {
        this.logo = "function call ";
        this.name = n;
    }

    @Override
    public String toString() {
        return this.logo + this.name;
    }

    public String getName() {
        return name;
    }
}
