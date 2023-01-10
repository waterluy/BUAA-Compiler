package syntaxanalysis.parser;

import mid.tree.FuncCallNode;
import mid.tree.OpExpNode;
import mid.tree.UnaryExpNode;
import syntaxanalysis.symtable.FunctionItem;
import syntaxanalysis.symtable.ReturnType;

public class UnaryExpParser extends Parser {
    private final String unit = "UnaryExp";
    private boolean isExp;

    public UnaryExpParser() {
        super();
        this.isExp = false;
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("+") || nowWord.getStr().equals("-")
                || nowWord.getStr().equals("!")) {
            this.treeNode = new OpExpNode(nowWord.getStr());
            new UnaryOpParser();
            UnaryExpParser unaryExp = new UnaryExpParser();
            ((OpExpNode) treeNode).setUnaryExp((UnaryExpNode) unaryExp.treeNode);
            this.dim = 0;
            this.isExp = unaryExp.isExp();
        } else if (nowWord.getStr().equals("(") || nowWord.getTypeCode().equals("INTCON")) {
            PrimaryExpParser primaryExp = new PrimaryExpParser();
            this.dim = primaryExp.getDim();
            this.isExp = primaryExp.isExp();
            this.treeNode = primaryExp.treeNode;
        } else if (nowWord.getTypeCode().equals("IDENFR")) {
            this.isExp = true;
            String word = lexer.getWord().getStr();
            lexer.goBack(1);
            if (word.equals("(")) {
                String funcName = nowWord.getStr(); //函数名
                this.treeNode = new FuncCallNode(funcName);
                int line = nowWord.getLine();   //函数名所在行
                FunctionItem f = checkNotDefFunc(nowWord);
                if ((f == null) || (f.getReturnType() == ReturnType.VOID)) {
                    this.dim = -1;
                } else {
                    this.dim = 0;
                }   //记录表达式的维度
                readWord(); // 读出(
                readWord();
                FuncRParamsParser rparams = null;
                if (!nowWord.getStr().equals(")") && !nowWord.getStr().equals(";") && !nowWord.getStr().equals(",")) {
                    rparams = new FuncRParamsParser(funcName);  //读实参
                    ((FuncCallNode) treeNode).addRparam(rparams.getRparamNodes());
                }
                int rparamNum = (rparams == null) ? 0 : rparams.getParamNum();  //函数实参个数
                if (f != null) {
                    if (rparamNum != f.getFparaNum()) { // 检查参数个数
                        handleError(line, "d", funcName);
                    } else if ((rparams != null) && (!f.getDims().equals(rparams.getDims()))) { //检查参数类型
                        handleError(line, "e", funcName);
                    }
                }
                if (nowWord.getStr().equals(")")) {
                    readWord();
                } else {
                    handleError(lexer.getLastWordLine(), "j", "{");
                }
            } else { // PrimaryExp LVal
                PrimaryExpParser primaryExp = new PrimaryExpParser();
                this.dim = primaryExp.getDim();
                this.isExp = primaryExp.isExp();
                this.treeNode = primaryExp.treeNode;
            }
        } else {
            this.isExp = false;
            error();
        }
    }

    public boolean isExp() {
        return isExp;
    }
}
