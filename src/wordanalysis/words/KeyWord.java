package wordanalysis.words;

import java.util.HashMap;
import java.util.regex.Pattern;

public class KeyWord extends Word {
    private static final HashMap<String, String> WORD2TYPE = new HashMap<String, String>() {
        {
            put("main", "MAINTK");
            put("const", "CONSTTK");
            put("int", "INTTK");
            put("break", "BREAKTK");
            put("continue", "CONTINUETK");
            put("if", "IFTK");
            put("else", "ELSETK");
            put("while", "WHILETK");
            put("getint", "GETINTTK");
            put("printf", "PRINTFTK");
            put("return", "RETURNTK");
            put("void", "VOIDTK");
        }
    };
    public static final Pattern PATTERN = Pattern.compile("^(main|const|int|break|continue|if|else|while|getint|printf|return|void)(\\W)");
    // 正则表达式匹配时的group(1)是匹配的一个 word
    private final String typeCode;

    public KeyWord(int line, String str) {
        super(line, str);
        this.typeCode = WORD2TYPE.get(str);
    }

    @Override
    public String getTypeCode() {
        return typeCode;
    }
}
