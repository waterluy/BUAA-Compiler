package syntaxanalysis.parser;

import mid.tree.EqExpNode;
import mid.tree.RelExpNode;

public class EqExpParser extends Parser {
    private final String unit = "EqExp";

    public EqExpParser() {
        super();
        this.treeNode = new EqExpNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        RelExpParser rel = new RelExpParser();
        ((EqExpNode) treeNode).addRel((RelExpNode) rel.treeNode);
        while (nowWord.getStr().equals("==") || nowWord.getStr().equals("!=")) {
            ((EqExpNode) treeNode).addOp(nowWord.getStr());
            output.addSyntax("<" + unit + ">");
            readWord();
            rel = new RelExpParser();
            ((EqExpNode) treeNode).addRel((RelExpNode) rel.treeNode);
        }
    }
}
