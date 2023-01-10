package syntaxanalysis.parser;

public class ExpParser extends Parser {
    private final String unit = "Exp";
    private boolean isExp;

    public ExpParser() {
        super();
        this.dim = 0;
        this.isExp = true;
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        AddExpParser add = new AddExpParser();
        this.dim = add.getDim();
        this.isExp = add.isExp();
        this.treeNode = add.treeNode;
    }

    public boolean isExp() {
        return isExp;
    }
}
