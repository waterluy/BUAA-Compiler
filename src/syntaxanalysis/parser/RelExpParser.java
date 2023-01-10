package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.RelExpNode;

public class RelExpParser extends Parser {
    private final String unit = "RelExp";

    public RelExpParser() {
        super();
        this.treeNode = new RelExpNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        AddExpParser addExpParser = new AddExpParser();
        ((RelExpNode) treeNode).addAddExp((AddExpNode) addExpParser.treeNode);
        while (nowWord.getStr().equals("<") || nowWord.getStr().equals(">")
                || nowWord.getStr().equals("<=") || nowWord.getStr().equals(">=")) {
            ((RelExpNode) treeNode).addOp(nowWord.getStr());
            output.addSyntax("<" + unit + ">");
            readWord();
            addExpParser = new AddExpParser();
            ((RelExpNode) treeNode).addAddExp((AddExpNode) addExpParser.treeNode);
        }
    }
}
