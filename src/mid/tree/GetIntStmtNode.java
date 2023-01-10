package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.GetInt;

public class GetIntStmtNode extends StmtNode implements TreeNode {
    private final LValNode lval;

    public GetIntStmtNode(LValNode l) {
        this.lval = l;
    }

    @Override
    public void generateMidCode() {
        lval.generateMidCode();
        AllMidCodes.addMidCode(new GetInt(lval.getLval()));
    }
}
