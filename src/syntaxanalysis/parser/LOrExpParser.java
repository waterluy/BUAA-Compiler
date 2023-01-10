package syntaxanalysis.parser;

import mid.tree.LandExpNode;
import mid.tree.LorExpNode;

public class LOrExpParser extends Parser {
    private final String unit = "LOrExp";

    public LOrExpParser() {
        super();
        this.treeNode = new LorExpNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        LAndExpParser lAndExpParser = new LAndExpParser();
        ((LorExpNode) treeNode).addLand((LandExpNode) lAndExpParser.treeNode);
        while (nowWord.getStr().equals("||")) {
            output.addSyntax("<" + unit + ">");
            readWord();
            lAndExpParser = new LAndExpParser();
            ((LorExpNode) treeNode).addLand((LandExpNode) lAndExpParser.treeNode);
        }
    }
}
