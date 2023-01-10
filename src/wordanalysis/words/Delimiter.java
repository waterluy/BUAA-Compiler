package wordanalysis.words;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Delimiter extends Word {
    private static final HashMap<String, String> WORD2TYPE = new HashMap<String, String>() {
        {
            put("&&", "AND");
            put("||", "OR");
            put("<=", "LEQ");
            put(">=", "GEQ");
            put("==", "EQL");
            put("!=", "NEQ");
            put("!", "NOT");
            put("+", "PLUS");
            put("-", "MINU");
            put("*", "MULT");
            put("/", "DIV");
            put("%", "MOD");
            put("<", "LSS");
            put(">", "GRE");
            put("=", "ASSIGN");
            put(";", "SEMICN");
            put(",", "COMMA");
            put("(", "LPARENT");
            put(")", "RPARENT");
            put("[", "LBRACK");
            put("]", "RBRACK");
            put("{", "LBRACE");
            put("}", "RBRACE");
        }
    };
    public static final Pattern PATTERN = Pattern.compile("^(&&|\\|\\||<=|>=|==|!=|!|\\+|-|\\*|/|%|<|>|=|;|,|\\(|\\)|\\[|]|\\{|})");
    private final String typeCode;

    public Delimiter(int line, String str) {
        super(line, str);
        this.typeCode = WORD2TYPE.get(str);
    }

    @Override
    public String getTypeCode() {
        return typeCode;
    }
}
