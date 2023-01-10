package syntaxanalysis.parser;

import syntaxanalysis.symtable.FunctionItem;
import syntaxanalysis.symtable.SymbolTable;
import syntaxanalysis.symtable.ReturnType;

public class MainFuncDefParser extends Parser {
    private final String unit = "MainFuncDef";
    private final SymbolTable nowTable;

    public MainFuncDefParser() {
        super();
        nowTable = TABLES.peek();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("int")) {
            readWord();
            if (nowWord.getStr().equals("main")) {
                checkReDef(nowTable, nowWord);
                FunctionItem funcItem = new FunctionItem("main", "int");
                nowTable.addFunc(funcItem);
                readWord();
                if (nowWord.getStr().equals("(")) {
                    readWord();
                    if (nowWord.getStr().equals(")")) {
                        readWord();
                    } else {
                        handleError(lexer.getLastWordLine(), "j", "{");
                    }
                    //error 入栈
                    TABLES.push(new SymbolTable(funcItem)); //error 入栈
                    BlockParser blockParser = new BlockParser(false, ReturnType.INT, true);
                    this.treeNode = blockParser.treeNode;
                    TABLES.pop();
                } else {
                    error();
                }
            } else {
                error();
            }
        } else {
            error();
        }
    }
}
