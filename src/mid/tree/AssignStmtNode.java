package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;

public class AssignStmtNode extends StmtNode implements TreeNode {
    private LValNode lval;
    private AddExpNode addExp;

    public AssignStmtNode(LValNode lval, AddExpNode addExp) {
        this.lval = lval;
        this.addExp = addExp;
    }

    @Override
    public void generateMidCode() {
        addExp.generateMidCode();
        lval.generateMidCode();
        BinaryExp binaryExp = new BinaryExp(lval.getLval(), false);
        binaryExp.setOperand1(addExp.getResult());
        AllMidCodes.addMidCode(binaryExp);
    }
}
