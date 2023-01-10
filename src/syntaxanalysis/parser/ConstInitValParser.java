package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.InitValNode;

import java.util.ArrayList;

public class ConstInitValParser extends Parser {
    private final String unit = "ConstInitVal";
    private final ArrayList<AddExpNode> initVals;

    public ConstInitValParser() {
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
                ConstInitValParser constInitValParser = new ConstInitValParser();
                initVals.addAll(constInitValParser.getInitVals());
                while (nowWord.getStr().equals(",")) {
                    readWord();
                    constInitValParser = new ConstInitValParser();
                    initVals.addAll(constInitValParser.getInitVals());
                }
            }
            if (nowWord.getStr().equals("}")) {
                readWord();
            } else {
                error();
            }
        } else {
            ConstExpParser constExpParser = new ConstExpParser();
            //mid tree node
            initVals.add((AddExpNode) constExpParser.treeNode);
        }
        ((InitValNode) treeNode).addInitVal(initVals);
    }

    public ArrayList<AddExpNode> getInitVals() {
        return initVals;
    }
}
