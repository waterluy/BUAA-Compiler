package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.InitValNode;

import java.util.ArrayList;

public class InitValParser extends Parser {
    private final String unit = "InitVal";
    private final ArrayList<AddExpNode> initVals;

    public InitValParser() {
        super();
        this.initVals = new ArrayList<>();
        this.treeNode = new InitValNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("{")) {
            readWord();
            if (!nowWord.getStr().equals("}")) {
                InitValParser initValParser = new InitValParser();
                initVals.addAll(initValParser.getInitVals());
                while (nowWord.getStr().equals(",")) {
                    readWord();
                    initValParser = new InitValParser();
                    initVals.addAll(initValParser.getInitVals());
                }
            }
            if (nowWord.getStr().equals("}")) {
                readWord();
            } else {
                error();
            }
        } else {
            ExpParser expParser = new ExpParser();
            //mid tree node
            initVals.add((AddExpNode) expParser.treeNode);
        }
        ((InitValNode) treeNode).addInitVal(initVals);
    }

    public ArrayList<AddExpNode> getInitVals() {
        return initVals;
    }
}
