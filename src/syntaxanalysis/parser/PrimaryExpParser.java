package syntaxanalysis.parser;

public class PrimaryExpParser extends Parser {
    private final String unit = "PrimaryExp";
    private boolean isExp;

    public PrimaryExpParser() {
        super();
        this.isExp = true;
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("(")) {
            readWord();
            ExpParser exp = new ExpParser();
            this.treeNode = exp.treeNode;
            this.isExp = exp.isExp();
            this.dim = exp.getDim();
            if (nowWord.getStr().equals(")")) {
                readWord();
            } else {    // (Exp)
                handleError(lexer.getLastWordLine(), "j", "exp{");
            }
        } else if (nowWord.getTypeCode().equals("IDENFR")) {
            LValParser lavl =  new LValParser();
            this.isExp = lavl.isExp();
            this.dim = lavl.getDim();
            this.treeNode = lavl.treeNode;
        } else {
            NumberParser num = new NumberParser();
            this.isExp = num.isExp();
            this.dim = 0;
            this.treeNode = num.treeNode;
        }
    }

    public boolean isExp() {
        return isExp;
    }
}
