package syntaxanalysis.parser;

import mid.tree.MulExpNode;
import mid.tree.UnaryExpNode;

public class MulExpParser extends Parser {
    private final String unit = "MulExp";
    private boolean isExp;

    public MulExpParser() {
        super();
        this.isExp = true;
        this.treeNode = new MulExpNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        UnaryExpParser unaryExp = new UnaryExpParser();
        this.dim = unaryExp.getDim();
        this.isExp = unaryExp.isExp();
        ((MulExpNode) treeNode).addUnary((UnaryExpNode) unaryExp.treeNode);
        while (nowWord.getStr().equals("*") || nowWord.getStr().equals("/")
                || nowWord.getStr().equals("%")) {
            ((MulExpNode) treeNode).addOp(nowWord.getStr());
            output.addSyntax("<" + unit + ">");
            readWord();
            unaryExp = new UnaryExpParser();
            ((MulExpNode) treeNode).addUnary((UnaryExpNode) unaryExp.treeNode);
        }
    }

    public boolean isExp() {
        return isExp;
    }
}
