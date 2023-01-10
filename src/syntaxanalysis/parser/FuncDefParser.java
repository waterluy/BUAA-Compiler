package syntaxanalysis.parser;

import mid.tree.BlockNode;
import mid.tree.FuncDefNode;
import mid.tree.FuncTypeNode;
import syntaxanalysis.symtable.FunctionItem;
import syntaxanalysis.symtable.ReturnType;
import syntaxanalysis.symtable.SymbolTable;

public class FuncDefParser extends Parser {
    private final String unit = "FuncDef";
    private final SymbolTable nowTable;
    private final String returnType;

    public FuncDefParser(String returnType) {
        super();
        this.returnType = returnType;
        nowTable = TABLES.peek();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        FuncTypeParser funcTypeParser = new FuncTypeParser();
        this.treeNode = new FuncDefNode((FuncTypeNode) funcTypeParser.treeNode, nowWord.getStr());
        if (nowWord.getTypeCode().equals("IDENFR")) {
            checkReDef(nowTable, nowWord);//检查函数是否重定义
            FunctionItem funcItem = new FunctionItem(nowWord.getStr(), returnType);
            nowTable.addFunc(funcItem); //把函数名+类型加进符号表
            readWord();
            if (nowWord.getStr().equals("(")) {
                //函数参数就要加入函数block的符号表
                TABLES.push(new SymbolTable(funcItem)); //error 入栈
                readWord();
                if (nowWord.getStr().equals("{")) { //缺少右括号)
                    handleError(lexer.getLastWordLine(), "j", "{");
                } else {
                    if (!nowWord.getStr().equals(")")) {
                        FuncFParamsParser funcFParamsParser = new FuncFParamsParser();
                        ((FuncDefNode) treeNode).addParam(funcFParamsParser.getFparamNodes());
                    }
                    if (nowWord.getStr().equals(")")) {
                        readWord();
                    } else {
                        handleError(lexer.getLastWordLine(), "j", "{");
                    }
                }
                BlockParser blockParser;
                if (returnType.equals("int")) {
                    blockParser = new BlockParser(false, ReturnType.INT, true);
                } else {
                    blockParser = new BlockParser(false, ReturnType.VOID, true);
                }
                //mid tree node block
                ((FuncDefNode) treeNode).setBlock((BlockNode) blockParser.treeNode);
                TABLES.pop();
            } else {
                error();
            }
        } else {
            error();
        }
    }
}
