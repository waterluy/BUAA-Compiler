package syntaxanalysis.parser;

import syntaxanalysis.symtable.ReturnType;
import tools.ReturnInf;

public class BlockItemParser extends Parser {
    private final String unit = "BlockItem";
    private final boolean isWhile;
    private ReturnInf returnInf;
    private int returnLine;
    private ReturnType returnType; //记录函数应该的返回类型

    public BlockItemParser(boolean isWhile, ReturnType returnType) {
        super();
        this.isWhile = isWhile;
        this.returnType = returnType;
        analyze();
        //no output.addLine("<" + unit + ">");
    }

    @Override
    public void analyze() {
        if (nowWord.getStr().equals("const") || nowWord.getStr().equals("int")) {
            this.returnInf = ReturnInf.RETURN_NONE;
            DeclParser declParser = new DeclParser();
            this.treeNode = declParser.treeNode;   //mid tree node
        } else {
            StmtParser stmtParser = new StmtParser(this.isWhile, this.returnType);
            this.treeNode = stmtParser.treeNode;
            this.returnInf = stmtParser.getReturnInf();
            if (returnInf != ReturnInf.RETURN_NONE) {
                this.returnLine = stmtParser.getReturnLine();   //获取return语句的行号
            }
        }
    }

    public ReturnInf getReturnInf() {
        return returnInf;
    }

    public int getReturnLine() {
        return returnLine;
    }
}
