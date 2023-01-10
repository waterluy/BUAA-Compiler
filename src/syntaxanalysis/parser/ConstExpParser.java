package syntaxanalysis.parser;

public class ConstExpParser extends Parser {
    private final String unit = "ConstExp";

    public ConstExpParser() {
        super();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        AddExpParser addExpParser = new AddExpParser();
        this.treeNode = addExpParser.treeNode;
    }
}
