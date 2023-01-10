package syntaxanalysis.parser;

public class UnaryOpParser extends Parser {
    private final String unit = "UnaryOp";

    public UnaryOpParser() {
        super();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("+") || nowWord.getStr().equals("-")
                || nowWord.getStr().equals("!")) {
            readWord();
        } else {
            error();
        }
    }
}
