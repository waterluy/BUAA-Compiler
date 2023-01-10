package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.Goto;

/**
 * break continue
 */

public class BCStmtNode extends StmtNode implements TreeNode {
    private final String logo;  //break continue

    public BCStmtNode(String s) {
        this.logo = s;
    }

    @Override
    public void generateMidCode() {
        if (this.logo.equals("break") && (this.labelWhileEnd != null)) {
            Goto gt = new Goto(labelWhileEnd.getLabel());
            AllMidCodes.addMidCode(gt);
        } else if (this.logo.equals("continue") && this.labelWhileBegin != null) {
            Goto gt = new Goto(labelWhileBegin.getLabel());
            AllMidCodes.addMidCode(gt);
        }
    }
}
