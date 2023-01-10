package syntaxanalysis.parser;

import mid.tree.BTypeNode;
import mid.tree.DeclNode;
import mid.tree.DefNode;

public class ConstDeclParser extends Parser {
    private final String unit = "ConstDecl";

    public ConstDeclParser() {
        super();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (!nowWord.getStr().equals("const")) {
            error();
        } else {
            readWord();
            BTypeParser btypeParser = new BTypeParser();
            this.treeNode = new DeclNode(true, (BTypeNode) btypeParser.treeNode);
            ConstDefParser constDefParser = new ConstDefParser();
            ((DeclNode) treeNode).addDef((DefNode) constDefParser.treeNode);   //mid
            while (true) {
                if (nowWord.getStr().equals(",")) {
                    readWord();
                    constDefParser = new ConstDefParser();
                    //mid tree node
                    ((DeclNode) treeNode).addDef((DefNode) constDefParser.treeNode);   //mid
                } else if (nowWord.getStr().equals(";")) {
                    readWord();
                    break;
                } else {
                    handleError(lexer.getLastWordLine(), "i", nowWord.getStr());
                    break;
                }
            }
        }
    }
}
