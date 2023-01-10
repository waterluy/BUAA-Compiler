package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

public class OpExpNode implements UnaryExpNode, TreeNode {
    private final String op;
    private UnaryExpNode unaryExp;

    private Val result;
    private boolean forConst;

    public boolean isForConst() {
        return forConst;
    }

    public void setForConst(boolean forConst) {
        this.forConst = forConst;
    }

    public OpExpNode(String o) {
        this.op = o;
    }

    public void setUnaryExp(UnaryExpNode unaryExp) {
        this.unaryExp = unaryExp;
    }

    @Override
    public void generateMidCode() {
        this.result = new TempVar();
        BinaryExp binaryExp = new BinaryExp(this.result, forConst);
        binaryExp.setOp(op);
        if (unaryExp instanceof AddExpNode) {   //!!!!!记得setForConst unaryexp
            ((AddExpNode) unaryExp).setForConst(forConst);
        } else if (unaryExp instanceof OpExpNode) {
            ((OpExpNode) unaryExp).setForConst(forConst);
        } else if (unaryExp instanceof LValNode) {
            ((LValNode) unaryExp).setForConst(forConst);
        }
        unaryExp.generateMidCode();
        binaryExp.setOperand2(unaryExp.getResult());
        AllMidCodes.addMidCode(binaryExp);
    }

    @Override
    public Val getResult() {
        return this.result;
    }
}
