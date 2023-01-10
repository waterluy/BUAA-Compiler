package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.*;
import mid.midcodes.labels.Body;
import mid.midcodes.val.Immediate;

import java.util.ArrayList;

public class LorExpNode implements TreeNode {
    private final ArrayList<LandExpNode> lands;
    private final String op = "||";

    private Body if_or_while_body;

    public LorExpNode() {
        this.lands = new ArrayList<>();
    }

    public void addLand(LandExpNode and) {
        this.lands.add(and);
    }

    @Override
    public void generateMidCode() {
        for (LandExpNode landExpNode : lands) {
            landExpNode.generateMidCode();
            CmpBranch cmpBranch = new CmpBranch("beq"); //r == 0 跳转
            cmpBranch.setOperand1(landExpNode.getResult());
            cmpBranch.setOperand2(new Immediate(0));
            cmpBranch.setLabel(if_or_while_body.getLabel());
            AllMidCodes.addMidCode(cmpBranch);  //bne eq1 0 IfBody(WhileBody)
        }
    }

    public void generateMidCode(boolean optimize, Body jumpLabel) {
        for (LandExpNode landExpNode : lands) {
            landExpNode.generateMidCode();
            CmpBranch cmpBranch = new CmpBranch("bne"); //r != 0 跳转
            cmpBranch.setOperand1(landExpNode.getResult());
            cmpBranch.setOperand2(new Immediate(0));
            cmpBranch.setLabel(jumpLabel.getLabel());
            AllMidCodes.addMidCode(cmpBranch);  //bne eq1 0 IfBody(WhileBody)
        }
    }

    public void setIf_or_while_body(Body if_or_while_body) {
        this.if_or_while_body = if_or_while_body;
    }
}
