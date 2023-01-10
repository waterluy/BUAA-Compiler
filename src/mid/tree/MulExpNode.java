package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class MulExpNode implements TreeNode {
    private final ArrayList<UnaryExpNode> unarys;
    private final ArrayList<String> op; // * /

    private Val result;
    private boolean forConst;

    public MulExpNode() {
        this.unarys = new ArrayList<>();
        this.op = new ArrayList<>();
    }

    public void setForConst(boolean forConst) {
        this.forConst = forConst;
    }

    public boolean isForConst() {
        return forConst;
    }

    public void addUnary(UnaryExpNode u) {
        unarys.add(u);
    }

    public void addOp(String o) {
        this.op.add(o);
    }

    @Override
    public void generateMidCode() {
        int l = unarys.size();
        if (l == 0) {
            System.out.println("mulexp unarys 0 error");
        }
        UnaryExpNode u = unarys.get(0);
        if (u instanceof AddExpNode) {
            ((AddExpNode) u).setForConst(forConst);
        } else if (u instanceof OpExpNode) {
            ((OpExpNode) u).setForConst(forConst);
        } else if (u instanceof LValNode) {
            ((LValNode) u).setForConst(forConst);
        }
        u.generateMidCode();
        Val tr = unarys.get(0).getResult();
        for (int i = 1; i < l; ++i) {
            u = unarys.get(i);
            if (u instanceof AddExpNode) {
                ((AddExpNode) u).setForConst(forConst);
            } else if (u instanceof OpExpNode) {
                //System.out.println(forConst);
                ((OpExpNode) u).setForConst(forConst);
            } else if (u instanceof LValNode) {
                ((LValNode) u).setForConst(forConst);
            }
            u.generateMidCode();
            TempVar temp = new TempVar();
            BinaryExp binaryExp = new BinaryExp(temp, forConst);
            binaryExp.setOperand1(tr);
            binaryExp.setOp(op.get(i - 1));
            binaryExp.setOperand2(u.getResult());
            AllMidCodes.addMidCode(binaryExp);
            tr = temp;
        }
        this.result = tr;
    }

    public Val getResult() {
        return this.result;
    }
}
