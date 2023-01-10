package mid.tree;

import mid.AllMidCodes;
import mid.CmpOp;
import mid.midcodes.*;
import mid.midcodes.labels.LabelEqEnd;
import mid.midcodes.labels.LabelEqFalse;
import mid.midcodes.val.Immediate;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class EqExpNode implements TreeNode {
    private final ArrayList<RelExpNode> rels;
    private final ArrayList<String> op; // ==   !=

    private Val result;

    public EqExpNode() {
        this.rels = new ArrayList<>();
        this.op = new ArrayList<>();
    }

    public void addRel(RelExpNode r) {
        this.rels.add(r);
    }

    public void addOp(String s) {
        this.op.add(s);
    }

    @Override
    public void generateMidCode() {
        int l = rels.size();
        rels.get(0).generateMidCode();
        Val tr = rels.get(0).getResult();
        for (int i = 1; i < l; ++i) {
            RelExpNode rel = rels.get(i);
            rel.generateMidCode();
            TempVar temp = new TempVar();
            BinaryExp binaryExp = new BinaryExp(temp, false);
            binaryExp.setOperand1(tr);
            binaryExp.setOp(op.get(i - 1));
            binaryExp.setOperand2(rel.getResult());
            AllMidCodes.addMidCode(binaryExp);
            tr = temp;
        }
        this.result = tr;
    }

    public Val getResult() {
        return result;
    }
}
