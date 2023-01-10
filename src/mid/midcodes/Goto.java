package mid.midcodes;

public class Goto implements MidCode {
    private final String logo;
    private final String label;

    public Goto(String l) {
        this.logo = "goto ";
        this.label = l;
    }

    @Override
    public String toString() {
        return this.logo + this.label;
    }

    public String getLabel() {
        return label;
    }
}
