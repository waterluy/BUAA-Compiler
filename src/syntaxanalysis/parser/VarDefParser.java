package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.DefNode;
import mid.tree.InitValNode;
import syntaxanalysis.symtable.SymbolTable;
import syntaxanalysis.symtable.VariableItem;

public class VarDefParser extends Parser {
    private final String unit = "VarDef";
    private SymbolTable nowTable;

    public VarDefParser() {
        super();
        nowTable = TABLES.peek();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (!nowWord.getTypeCode().equals("IDENFR")) {
            error();
        } else {
            //mid tree node
            this.treeNode = new DefNode(nowWord.getStr(), false);

            //fill in the table
            VariableItem varItem = null;
            boolean reDef = checkReDef(nowTable, nowWord);
            if (!reDef) {
                varItem = new VariableItem(nowWord.getStr(), false);
            }

            readWord();
            for (int i = 0; i < 2; ++i) {
                if (nowWord.getStr().equals("[")) {
                    if (varItem != null) {
                        varItem.addDim();
                    }

                    readWord();
                    ConstExpParser constExpParser = new ConstExpParser();
                    ((DefNode) treeNode).addDim((AddExpNode) constExpParser.treeNode);
                    if (nowWord.getStr().equals("]")) {
                        readWord();
                    } else {
                        handleError(lexer.getLastWordLine(), "k", nowWord.getStr());
                    }
                } else {
                    break;
                }
            }

            if (nowWord.getStr().equals("=")) {
                readWord();
                InitValParser initValParser = new InitValParser();
                ((DefNode) treeNode).setInitVaL((InitValNode) initValParser.treeNode);
            }

            if (varItem != null) {
                nowTable.addVar(varItem);
            }
        }
    }
}
