package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.FuncFpara;
import mid.midcodes.FunctionDef;

import java.util.ArrayList;

public class FuncDefNode implements TreeNode {
    private final FuncTypeNode funcType;
    private final String ident;
    private final ArrayList<FparamNode> fparams;
    private BlockNode block;

    public FuncDefNode(FuncTypeNode t, String i) {
        this.funcType = t;
        this.ident = i;
        this.fparams = new ArrayList<>();
    }

    public void addParam(ArrayList<FparamNode> a) {
        this.fparams.addAll(a);
    }

    public void setBlock(BlockNode b) {
        this.block = b;
        this.block.setFunc();
    }

    @Override
    public void generateMidCode() {
        FunctionDef functionDef = new FunctionDef(funcType.getType(), this.ident, !fparams.isEmpty());
        for (FparamNode p : fparams) {
            p.generateMidCode();
            functionDef.addParm(p.getMidCode());
        }
        AllMidCodes.addMidCode(functionDef);
        for (FuncFpara paraCode : functionDef.getParas()) {
            AllMidCodes.addMidCode(paraCode);
        }
        if (funcType.getType().equals("void")) {
            block.setVoidFunc(true);
        }
        block.generateMidCode();
    }
}
