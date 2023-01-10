package syntaxanalysis.parser;

import mid.tree.EqExpNode;
import mid.tree.LandExpNode;

public class LAndExpParser extends Parser {
    private final String unit = "LAndExp";

    public LAndExpParser() {
        super();
        this.treeNode = new LandExpNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        EqExpParser eqExpParser = new EqExpParser();
        ((LandExpNode) treeNode).addEq((EqExpNode) eqExpParser.treeNode);
        while (nowWord.getStr().equals("&&")) {
            output.addSyntax("<" + unit + ">");
            readWord();
            eqExpParser = new EqExpParser();
            ((LandExpNode) treeNode).addEq((EqExpNode) eqExpParser.treeNode);
        }
    }
}
