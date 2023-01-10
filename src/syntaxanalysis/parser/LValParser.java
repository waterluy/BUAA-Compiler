package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.LValNode;
import syntaxanalysis.symtable.VariableItem;

public class LValParser extends Parser {
    private final String unit = "LVal";
    private int length = 0;
    private String error = null;
    private boolean isExp;

    public LValParser() {
        super();
        this.isExp = true;
        int beginIndex = lexer.getWordIndex();
        analyze();
        this.length = lexer.getWordIndex() - beginIndex + 1;
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getTypeCode().equals("IDENFR")) {
            this.treeNode = new LValNode(nowWord.getStr());
            VariableItem v = checkNotDefVar(nowWord);    // v==null -> notDef
            if (v == null) {
                this.error = nowWord.getLine() + " " + "c";
            }
            readWord();
            int rdim = 0;
            for (int i = 0; i < 2; ++i) {
                if (nowWord.getStr().equals("[")) {
                    readWord();
                    ExpParser expParser = new ExpParser();
                    ((LValNode) treeNode).addDim((AddExpNode) expParser.treeNode);
                    rdim++;
                    if (nowWord.getStr().equals("]")) {
                        readWord();
                    } else {
                        handleError(lexer.getLastWordLine(), "k", nowWord.getStr());
                        if (error == null) {
                            this.error = lexer.getLastWordLine() + " " + "k";
                        }
                    }
                } else {
                    break;
                }
            }
            this.dim = (v == null) ? 0 : (v.getDim() - rdim);
        } else {
            this.isExp = false;
            error();
        }
    }

    public int getLength() {
        return length;
    }

    public String getError() {
        return error;
    }

    public boolean isExp() {
        return isExp;
    }
}
