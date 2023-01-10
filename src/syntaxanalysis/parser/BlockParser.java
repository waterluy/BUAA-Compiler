package syntaxanalysis.parser;

import mid.tree.BlockItem;
import mid.tree.BlockNode;
import syntaxanalysis.symtable.ReturnType;
import tools.ReturnInf;

public class BlockParser extends Parser {
    private final String unit = "Block";
    private final boolean isWhile;
    private final ReturnType returnType;
    private boolean isLast; //标记是否是函数的最外层{}

    public BlockParser(boolean isWhile, ReturnType type, boolean isLast) {
        super();
        this.isWhile = isWhile;
        this.returnType = type;
        this.isLast = isLast;
        this.treeNode = new BlockNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        //TABLES.push(new SymTable()); //error 入栈
        if (nowWord.getStr().equals("{")) {
            readWord();
            BlockItemParser blockItemParser = null;
            while (!nowWord.getStr().equals("}")) {
                blockItemParser = new BlockItemParser(this.isWhile, this.returnType);
                ((BlockNode) treeNode).addBlockItem((BlockItem) blockItemParser.treeNode);
            }
            if (nowWord.getStr().equals("}")) {
                if ((returnType == ReturnType.INT) && isLast &&  //有返回值的函数 并且是函数的最外层}
                        ((blockItemParser == null) || (blockItemParser.getReturnInf() != ReturnInf.RETURN_EXP))) {
                    handleError(nowWord.getLine(), "g", "}");
                }
                readWord();
            } else {
                error();
            }
        } else {
            error();
        }
        //TABLES.pop();
    }
}
