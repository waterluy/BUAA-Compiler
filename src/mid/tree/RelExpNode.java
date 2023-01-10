package mid.tree;

import mid.AllMidCodes;
import mid.CmpOp;
import mid.midcodes.BinaryExp;
import mid.midcodes.CmpBranch;
import mid.midcodes.Goto;
import mid.midcodes.labels.LabelRelEnd;
import mid.midcodes.labels.LabelRelFalse;
import mid.midcodes.val.Immediate;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class RelExpNode implements TreeNode {
    private final ArrayList<AddExpNode> adds;
    private final ArrayList<String> op;

    private Val result;

    public RelExpNode() {
        this.adds = new ArrayList<>();
        this.op = new ArrayList<>();
    }

    public void addAddExp(AddExpNode a) {
        this.adds.add(a);
    }

    public void addOp(String s) {
        this.op.add(s);
    }

    @Override
    public void generateMidCode() {
        int l = adds.size();
        adds.get(0).generateMidCode();
        Val tr = adds.get(0).getResult();
        for (int i = 1; i < l; ++i) {
            AddExpNode add = adds.get(i);
            add.generateMidCode();
            TempVar temp = new TempVar();
            BinaryExp binaryExp = new BinaryExp(temp, false);
            binaryExp.setOperand1(tr);
            binaryExp.setOp(op.get(i - 1));
            binaryExp.setOperand2(add.getResult());
            AllMidCodes.addMidCode(binaryExp);
            tr = temp;
        }
        this.result = tr;
    }

    public Val getResult() {
        return this.result;
    }
}
