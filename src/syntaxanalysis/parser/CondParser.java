package syntaxanalysis.parser;

public class CondParser extends Parser {
    final String unit = "Cond";

    public CondParser() {
        super();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        LOrExpParser lOrExpParser = new LOrExpParser();
        this.treeNode = lOrExpParser.treeNode;
    }
}
