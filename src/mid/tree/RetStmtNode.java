package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.Return;

public class RetStmtNode extends StmtNode implements TreeNode {
    private final AddExpNode exp;

    public RetStmtNode() {
        this.exp = null;
    }

    public RetStmtNode(AddExpNode a) {
        this.exp = a;
    }

    @Override
    public void generateMidCode() {
        if (exp == null) {
            AllMidCodes.addMidCode(new Return());
        } else {
            exp.generateMidCode();
            AllMidCodes.addMidCode(new Return(exp.getResult()));
        }
    }
}
