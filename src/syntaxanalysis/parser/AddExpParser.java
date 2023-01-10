package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.MulExpNode;

public class AddExpParser extends Parser {
    private final String unit = "AddExp";
    //protected int dim;
    private boolean isExp;

    public AddExpParser() {
        super();
        this.isExp = true;
        this.treeNode = new AddExpNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        MulExpParser mul = new MulExpParser();
        ((AddExpNode) treeNode).addMul((MulExpNode) mul.treeNode);
        this.dim = mul.getDim();
        this.isExp = mul.isExp();
        while (nowWord.getStr().equals("+") || nowWord.getStr().equals("-")) {
            ((AddExpNode) treeNode).addOp(nowWord.getStr());
            output.addSyntax("<" + unit + ">");
            readWord();
            mul = new MulExpParser();
            ((AddExpNode) treeNode).addMul((MulExpNode) mul.treeNode);
        }
    }

    public boolean isExp() {
        return isExp;
    }
}
