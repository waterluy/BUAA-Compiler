package syntaxanalysis.parser;

import mid.tree.NumNode;

public class NumberParser extends Parser {
    private final String unit = "Number";
    private boolean isExp;

    public NumberParser() {
        super();
        this.isExp = true;
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getTypeCode().equals("INTCON")) {
            this.treeNode = new NumNode(Integer.parseInt(nowWord.getStr()));
            readWord();
        } else {
            this.isExp = false;
            error();
        }
    }

    public boolean isExp() {
        return isExp;
    }
}
