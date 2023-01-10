package wordanalysis.words;

import java.util.regex.Pattern;

public class Ident extends Word {
    public static final Pattern PATTERN = Pattern.compile("^([A-Z_a-z]\\w*)");
    private final String typeCode = "IDENFR";

    public Ident(int line, String str) {
        super(line, str);
    }

    @Override
    public String getTypeCode() {
        return typeCode;
    }
}
