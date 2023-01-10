package wordanalysis.words;

import java.util.regex.Pattern;

public class FormatStr extends Word {
    public static final Pattern PATTERN = Pattern.compile("^(\"[^\"]*\")");
    private final String typeCode = "STRCON";

    public FormatStr(int line, String str) {
        super(line, str);
    }

    @Override
    public String getTypeCode() {
        return typeCode;
    }
}
