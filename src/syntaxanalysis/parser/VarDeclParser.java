package syntaxanalysis.parser;

import mid.tree.BTypeNode;
import mid.tree.DeclNode;
import mid.tree.DefNode;

public class VarDeclParser extends Parser {
    private final String unit = "VarDecl";

    public VarDeclParser() {
        super();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        BTypeParser btypeParser = new BTypeParser();
        this.treeNode = new DeclNode(false, (BTypeNode) btypeParser.treeNode);
        VarDefParser varDefParser = new VarDefParser();
        ((DeclNode) treeNode).addDef((DefNode) varDefParser.treeNode);
        while (true) {
            if (nowWord.getStr().equals(",")) {
                readWord();
                varDefParser = new VarDefParser();
                ((DeclNode) treeNode).addDef((DefNode) varDefParser.treeNode);
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
