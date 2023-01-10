package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.Goto;
import mid.midcodes.labels.LabelElse;
import mid.midcodes.labels.LabelIfBody;
import mid.midcodes.labels.LabelIfEnd;
import mid.midcodes.labels.LabelWhileBegin;
import mid.midcodes.labels.LabelWhileBody;
import mid.midcodes.labels.LabelWhileEnd;

public class ControlStmtNode extends StmtNode implements TreeNode {
    private final String logo;      //if 或 while
    private LorExpNode cond;
    private StmtNode okStmt;
    private StmtNode elseStmt;//stmt可以为null

    public ControlStmtNode(String s) {
        this.logo = s;
        this.okStmt = null;
        this.elseStmt = null;
    }

    public void setCond(LorExpNode c) {
        this.cond = c;
    }

    public void setOkStmt(StmtNode okStmt) {
        this.okStmt = okStmt;
    }

    public void setElseStmt(StmtNode es) {
        this.elseStmt = es;
    }

    @Override
    public void generateMidCode() {
        if (logo.equals("if")) {
            ifMidCode();
        } else if (logo.equals("while")) {
            whileMidCode();
        }
    }

    public void ifMidCode() {
        LabelIfBody labelIfBody = new LabelIfBody();
        LabelElse labelElse = null;
        LabelIfEnd labelIfEnd = new LabelIfEnd();
        Goto gt;
        if (elseStmt != null) {
            labelElse = new LabelElse();
            cond.setIf_or_while_body(labelElse);
        } else {
            cond.setIf_or_while_body(labelIfEnd);
        }
        cond.generateMidCode();
        AllMidCodes.addMidCode(labelIfBody);    //IfBody1:
        if (okStmt != null) {       // if () ;
            okStmt.setLabelWhileBegin(this.labelWhileBegin);
            okStmt.setLabelWhileEnd(this.labelWhileEnd);
            okStmt.generateMidCode();
        }
        if (elseStmt != null) {
            gt = new Goto(labelIfEnd.getLabel());
            AllMidCodes.addMidCode(gt); // goto IfEnd1
            AllMidCodes.addMidCode(labelElse);  //Else1:
            elseStmt.setLabelWhileBegin(this.labelWhileBegin);
            elseStmt.setLabelWhileEnd(this.labelWhileEnd);
            elseStmt.generateMidCode();
        }
        AllMidCodes.addMidCode(labelIfEnd);
    }

    public void whileMidCode() {
        LabelWhileBegin labelWhileBegin = new LabelWhileBegin();
        AllMidCodes.addMidCode(labelWhileBegin);    //WhileBegin1:
        LabelWhileBody labelWhileBody = new LabelWhileBody();
        LabelWhileEnd labelWhileEnd = new LabelWhileEnd();
        cond.setIf_or_while_body(labelWhileEnd);
        cond.generateMidCode();
        Goto gt = new Goto(labelWhileEnd.getLabel());
        //AllMidCodes.addMidCode(gt); //goto WhileEnd1
        AllMidCodes.addMidCode(labelWhileBody);
        if (okStmt != null) {
            okStmt.setLabelWhileBegin(labelWhileBegin);
            okStmt.setLabelWhileEnd(labelWhileEnd);
            okStmt.generateMidCode();
        }

        cond.generateMidCode(true, labelWhileBody);

        AllMidCodes.addMidCode(labelWhileEnd);
    }
}
