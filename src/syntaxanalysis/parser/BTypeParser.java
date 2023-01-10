package syntaxanalysis.parser;

import mid.tree.BTypeNode;

public class BTypeParser extends Parser {
    private final String unit = "BType";

    public BTypeParser() {
        super();
        analyze();
        // no <BType>
    }

    @Override
    public void analyze() {
        if (!nowWord.getStr().equals("int")) {
            error();
        } else {
            this.treeNode = new BTypeNode(nowWord.getStr());
            readWord();
        }
    }
}
