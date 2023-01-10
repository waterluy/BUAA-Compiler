package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.DefNode;
import mid.tree.InitValNode;
import syntaxanalysis.symtable.SymbolTable;
import syntaxanalysis.symtable.VariableItem;

public class ConstDefParser extends Parser {
    private final String unit = "ConstDef";
    private SymbolTable nowTable;

    public ConstDefParser() {
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
            this.treeNode = new DefNode(nowWord.getStr(), true);

            //fill in the table
            VariableItem varItem = null;
            boolean reDef = checkReDef(nowTable, nowWord);
            if (!reDef) {
                varItem = new VariableItem(nowWord.getStr(), true);
            }

            readWord();
            for (int i = 0; i < 2; ++i) {
                if (nowWord.getStr().equals("[")) {
                    if (varItem != null) {
                        varItem.addDim();//记录数组维度
                    }

                    readWord();
                    ConstExpParser constExpParser = new ConstExpParser();
                    ((DefNode) treeNode).addDim((AddExpNode) constExpParser.treeNode); //mid tree node
                    if (nowWord.getStr().equals("]")) {
                        readWord();
                    } else { //error k
                        handleError(lexer.getLastWordLine(), "k", nowWord.getStr());
                    }
                } else {
                    break;
                }
            }

            if (nowWord.getStr().equals("=")) {
                readWord();
                ConstInitValParser constInitValParser = new ConstInitValParser();
                //mid tree node
                ((DefNode) treeNode).setInitVaL((InitValNode) constInitValParser.treeNode);
            } else {
                error();
            }
            if (varItem != null) { //要放在最后加，比如 a = a + 1
                nowTable.addVar(varItem);
            }
        }
    }
}
