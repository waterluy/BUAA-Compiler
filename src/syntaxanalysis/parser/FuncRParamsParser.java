package syntaxanalysis.parser;

import mid.tree.AddExpNode;

import java.util.ArrayList;

public class FuncRParamsParser extends Parser {
    private final String unit = "FuncRParams";
    private final String funcName;
    private int paramNum;
    private final ArrayList<Integer> dims;
    private final ArrayList<AddExpNode> rparamNodes;

    public FuncRParamsParser(String func) {
        super();
        this.funcName = func;
        this.paramNum = 0;
        this.dims = new ArrayList<>();
        this.rparamNodes = new ArrayList<>();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        ExpParser exp = new ExpParser();
        dims.add(exp.getDim());
        paramNum++;
        this.rparamNodes.add((AddExpNode) exp.treeNode);
        while (nowWord.getStr().equals(",")) {
            readWord();
            exp = new ExpParser();
            dims.add(exp.getDim());
            paramNum++;
            this.rparamNodes.add((AddExpNode) exp.treeNode);
        }
    }

    public int getParamNum() {
        return paramNum;
    }

    public ArrayList<Integer> getDims() {
        return dims;
    }

    public ArrayList<AddExpNode> getRparamNodes() {
        return rparamNodes;
    }
}
