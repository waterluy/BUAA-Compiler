package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.BTypeNode;
import mid.tree.FparamNode;
import syntaxanalysis.symtable.SymbolTable;
import syntaxanalysis.symtable.VariableItem;

public class FuncFParamParser extends Parser {
    private final String unit = "FuncFParam";
    private final SymbolTable nowTable;

    public FuncFParamParser() {
        super();
        this.nowTable = TABLES.peek();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        BTypeParser bTypeParser = new BTypeParser();
        if (nowWord.getTypeCode().equals("IDENFR")) {
            //mid tree node
            this.treeNode = new FparamNode((BTypeNode) bTypeParser.treeNode, nowWord.getStr());
            VariableItem varItem = new VariableItem(nowWord.getStr(), false);
            //给函数所在符号表添加
            if (!checkReDef(nowTable, nowWord)) {
                nowTable.addVar(varItem);
            }
            readWord();
            if (nowWord.getStr().equals("[")) {
                varItem.addDim(); //参数为一维数组
                ((FparamNode) treeNode).addDim();    //mid tree node
                readWord();
                if (nowWord.getStr().equals("]")) {
                    readWord();
                    if (nowWord.getStr().equals("[")) {
                        varItem.addDim(); //参数为二维数组
                        ((FparamNode) treeNode).addDim();   //mid tree node
                        readWord();
                        ConstExpParser constExpParser = new ConstExpParser();
                        ((FparamNode) treeNode).setLen_2dim((AddExpNode) constExpParser.treeNode);
                        if (nowWord.getStr().equals("]")) {
                            readWord();
                        } else {
                            handleError(lexer.getLastWordLine(), "k", nowWord.getStr());
                        }
                    }
                } else {
                    handleError(lexer.getLastWordLine(), "k", nowWord.getStr());
                }
            }
            //给函数增加参数信息
            nowTable.getFunc().addPara(varItem);
        } else {
            error();
        }
    }
}
