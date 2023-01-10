package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class AddExpNode extends StmtNode implements TreeNode, PrimaryExpNode, BlockItem {
    private final ArrayList<MulExpNode> muls;
    private final ArrayList<String> op; // + -

    private Val result;
    private boolean forConst;

    public AddExpNode() {
        this.muls = new ArrayList<>();
        this.op = new ArrayList<>();
        this.result = null;
    }

    public void setForConst(boolean forConst) {
        this.forConst = forConst;
    }

    public boolean isForConst() {
        return forConst;
    }

    public void addMul(MulExpNode m) {
        muls.add(m);
    }

    public void addOp(String o) {
        this.op.add(o);
    }

    @Override
    public void generateMidCode() {
        int l = muls.size();
        if (l == 0) {
            System.out.println("addexp muls 0 error");
        }
        muls.get(0).setForConst(forConst);
        muls.get(0).generateMidCode();
        Val tr = muls.get(0).getResult();
        for (int i = 1; i < l; ++i) {
            MulExpNode m = muls.get(i);
            m.setForConst(forConst);
            m.generateMidCode();
            TempVar temp = new TempVar();
            BinaryExp binaryExp = new BinaryExp(temp, forConst);
            binaryExp.setOperand1(tr);
            binaryExp.setOp(op.get(i - 1));
            binaryExp.setOperand2(m.getResult());
            AllMidCodes.addMidCode(binaryExp);
            tr = temp;
        }
        this.result = tr;
    }

    public Val getResult() {
        return this.result;
    }
}
