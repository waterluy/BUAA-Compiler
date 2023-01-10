package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.CmpBranch;
import mid.midcodes.Goto;
import mid.midcodes.labels.LabelAndEnd;
import mid.midcodes.labels.LabelAndFalse;
import mid.midcodes.val.Immediate;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class LandExpNode implements TreeNode {
    private final ArrayList<EqExpNode> eqs;
    private final String op = "&&";

    private Val result;

    public LandExpNode() {
        this.eqs = new ArrayList<>();
    }

    public void addEq(EqExpNode eq) {
        this.eqs.add(eq);
    }

    @Override
    public void generateMidCode() {
        LabelAndFalse labelAndFalse = new LabelAndFalse();  //只有一个and false！！！
        for (EqExpNode eqExpNode : eqs) {
            eqExpNode.generateMidCode();
            CmpBranch cmpBranch = new CmpBranch("beq"); //r == 0 跳转
            cmpBranch.setOperand1(eqExpNode.getResult());
            cmpBranch.setOperand2(new Immediate(0));
            cmpBranch.setLabel(labelAndFalse.getLabel());
            AllMidCodes.addMidCode(cmpBranch);  //beq eq1 0 AndFalse1
        }
        this.result = new TempVar();
        BinaryExp binaryExp = new BinaryExp(this.result, false);
        binaryExp.setOperand1(new Immediate(1));
        AllMidCodes.addMidCode(binaryExp);  //ret_and = 1
        LabelAndEnd labelAndEnd = new LabelAndEnd();
        Goto gt = new Goto(labelAndEnd.getLabel());
        AllMidCodes.addMidCode(gt); //goto AndEnd1
        AllMidCodes.addMidCode(labelAndFalse);  //AndFalse:
        binaryExp = new BinaryExp(this.result, false);
        binaryExp.setOperand1(new Immediate(0));
        AllMidCodes.addMidCode(binaryExp);  //ret_and = 0
        AllMidCodes.addMidCode(labelAndEnd);
    }

    public Val getResult() {
        return result;
    }
}
