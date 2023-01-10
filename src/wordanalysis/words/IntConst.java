package wordanalysis.words;

import java.util.regex.Pattern;

public class IntConst extends Word {
    public static final Pattern PATTERN = Pattern.compile("^([1-9]\\d*|0)");
    private final String typeCode = "INTCON";

    public IntConst(int line, String str) {
        super(line, str);
    }

    @Override
    public String getTypeCode() {
        return typeCode;
    }
}
