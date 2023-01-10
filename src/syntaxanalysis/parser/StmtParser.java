package syntaxanalysis.parser;

import mid.tree.AddExpNode;
import mid.tree.AssignStmtNode;
import mid.tree.BCStmtNode;
import mid.tree.BlockNode;
import mid.tree.BlockStmtNode;
import mid.tree.ControlStmtNode;
import mid.tree.GetIntStmtNode;
import mid.tree.LValNode;
import mid.tree.LorExpNode;
import mid.tree.PrintfStmtNode;
import mid.tree.RetStmtNode;
import mid.tree.StmtNode;
import syntaxanalysis.symtable.SymbolTable;
import syntaxanalysis.symtable.ReturnType;
import tools.ReturnInf;
import wordanalysis.words.Word;

public class StmtParser extends Parser {
    private final String unit = "Stmt";
    private final boolean isWhile;
    private ReturnInf returnInf;    //记录实际的返回值类型
    private int returnLine;
    private ReturnType returnType;  //记录应有的返回值类型

    public StmtParser(boolean isWhile, ReturnType returnType) {
        super();
        this.isWhile = isWhile;
        this.returnType = returnType;
        this.returnInf = ReturnInf.RETURN_NONE;
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals(";")) {
            readWord();
        } else if (nowWord.getStr().equals("{")) {
            TABLES.push(new SymbolTable()); //error 入栈
            BlockParser blockParser = new BlockParser(this.isWhile, this.returnType, false); //stmt_block not func_block
            if (!((BlockNode) blockParser.treeNode).isEmpty()) {    //mid tree node
                this.treeNode = new BlockStmtNode((BlockNode) blockParser.treeNode);
            }
            TABLES.pop();
        } else if (nowWord.getStr().equals("if")) {
            this.treeNode = new ControlStmtNode("if");
            checkIf();
        } else if (nowWord.getStr().equals("while")) {
            this.treeNode = new ControlStmtNode("while");
            checkWhile();
        } else if (nowWord.getStr().equals("break") || nowWord.getStr().equals("continue")) {
            this.treeNode = new BCStmtNode(nowWord.getStr());
            if (!isWhile) { //error while block
                handleError(nowWord.getLine(), "m", nowWord.getStr());
            }
            readWord();
            checkSemicn();
        } else if (nowWord.getStr().equals("return")) {
            this.returnInf = ReturnInf.RETURN_VOID;
            this.treeNode = new RetStmtNode();
            this.returnLine = nowWord.getLine();
            readWord();
            if (!nowWord.getStr().equals(";") && !nowWord.getStr().equals("}")) {
                ExpParser exp = new ExpParser();
                if (exp.isExp()) {  // return     while().....
                    this.returnInf = ReturnInf.RETURN_EXP;
                    this.treeNode = new RetStmtNode((AddExpNode) exp.treeNode);
                }
            }   //记录实际返回信息
            if ((this.returnType == ReturnType.VOID) && (this.returnInf == ReturnInf.RETURN_EXP)) {
                handleError(this.returnLine,  "f", "return");
            }   //比较应该的返回类型和实际的返回信息 进行错误处理
            checkSemicn();
        } else if (nowWord.getStr().equals("printf")) {
            this.treeNode = new PrintfStmtNode();
            checkPrintf(nowWord.getLine());
        } else {
            if (!nowWord.getTypeCode().equals("IDENFR")) { // [Exp];   mid_pass
                ExpParser expParser = new ExpParser();
                this.treeNode = expParser.treeNode;
                checkSemicn();
            } else { //nowWord.getTypeCode().equals("IDENFR")
                String backWord = nowWord.getTypeCode() + " " + nowWord.getStr();
                Word word = nowWord;    //记录了ident的名字
                LValParser lval = new LValParser();
                if (nowWord.getStr().equals("=")) {
                    checkConstAssign(word);
                    readWord();
                    if (nowWord.getStr().equals("getint")) {
                        this.treeNode = new GetIntStmtNode((LValNode) lval.treeNode);
                        readWord();
                        if (nowWord.getStr().equals("(")) {
                            readWord();
                            if (nowWord.getStr().equals(")")) {
                                readWord();
                            } else {
                                handleError(lexer.getLastWordLine(), "j", "{");
                            }
                        } else {
                            error();
                        }
                    } else {
                        ExpParser exp = new ExpParser();
                        this.treeNode = new AssignStmtNode((LValNode) lval.treeNode, (AddExpNode) exp.treeNode);
                    }
                    checkSemicn();
                } else {    //[Exp];  mid pass
                    //回退一个lval
                    lexer.goBack(lval.getLength());
                    output.goBackWord(backWord); //!!!!
                    if (lval.getError() != null) {
                        output.goBackError(lval.getError());
                    }
                    readWord();
                    ExpParser expParser = new ExpParser();
                    this.treeNode = expParser.treeNode;//[Exp];  mid pass
                    checkSemicn();
                }
            }
        }
    }

    private void checkSemicn() {
        if (nowWord.getStr().equals(";")) {
            readWord();
        } else {
            handleError(lexer.getLastWordLine(), "i", nowWord.getStr());
        }
    }

    private void checkIf() {
        readWord();
        if (nowWord.getStr().equals("(")) {
            readWord();
            CondParser condParser = new CondParser();
            ((ControlStmtNode) treeNode).setCond((LorExpNode) condParser.treeNode);
            if (nowWord.getStr().equals(")")) {
                readWord();
            } else {
                handleError(lexer.getLastWordLine(), "j", "{");
            }
            StmtParser stmtParser = new StmtParser(this.isWhile, this.returnType);
            ((ControlStmtNode) treeNode).setOkStmt((StmtNode) stmtParser.treeNode);
            if (nowWord.getStr().equals("else")) {
                readWord();
                stmtParser = new StmtParser(this.isWhile, this.returnType);
                ((ControlStmtNode) treeNode).setElseStmt((StmtNode) stmtParser.treeNode);
            }
        } else {
            error();
        }
    }

    private void checkWhile() {
        readWord();
        if (nowWord.getStr().equals("(")) {
            readWord();
            CondParser condParser = new CondParser();
            ((ControlStmtNode) treeNode).setCond((LorExpNode) condParser.treeNode);
            if (nowWord.getStr().equals(")")) {
                readWord();
            } else {
                handleError(lexer.getLastWordLine(), "j", "{");
            }
            StmtParser stmtParser = new StmtParser(true, this.returnType);
            ((ControlStmtNode) treeNode).setOkStmt((StmtNode) stmtParser.treeNode);
        } else {
            error();
        }
    }

    private void checkPrintf(int printfLine) { //nowWord -> "printf"
        readWord();
        if (nowWord.getStr().equals("(")) {
            readWord();
            if (nowWord.getTypeCode().equals("STRCON")) {  //,
                ((PrintfStmtNode) treeNode).setFormatStr(nowWord.getStr()); //mid tree node
                int formatCharNum = checkFormatStr();//error check a
                int expNum = 0;
                readWord();
                while (true) {
                    if (nowWord.getStr().equals(",")) {
                        readWord();
                        ExpParser expParser = new ExpParser();
                        expNum++;
                        ((PrintfStmtNode) treeNode).addExp((AddExpNode) expParser.treeNode);     //mid tree node
                    } else if (nowWord.getStr().equals(")")) {
                        readWord();
                        break;
                    } else {
                        handleError(lexer.getLastWordLine(), "j", "{");
                        break;
                    }
                }
                if (formatCharNum != expNum) {
                    handleError(printfLine, "l", String.format("formatCharNum: %d, expNum: %d", formatCharNum, expNum));
                }
                checkSemicn();
            } else {
                error();
            }
        } else {
            error();
        }
    }

    public ReturnInf getReturnInf() {
        return returnInf;
    }

    public int getReturnLine() {
        return returnLine;
    }
}
