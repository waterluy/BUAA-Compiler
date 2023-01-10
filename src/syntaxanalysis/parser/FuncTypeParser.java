package syntaxanalysis.parser;

import mid.tree.FuncTypeNode;

public class FuncTypeParser extends Parser {
    private final String unit = "FuncType";

    public FuncTypeParser() {
        super();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("void") || nowWord.getStr().equals("int")) {
            this.treeNode = new FuncTypeNode(nowWord.getStr());    //mid tree node
            readWord();
        } else {
            error();
        }
    }
}
