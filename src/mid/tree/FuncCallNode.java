package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.FunctionCall;
import mid.midcodes.PushRpara;
import mid.midcodes.val.RetValue;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class FuncCallNode implements TreeNode, UnaryExpNode {
    private final String ident;
    private final ArrayList<AddExpNode> rparams; //exp

    private Val result;

    public FuncCallNode(String i) {
        this.ident = i;
        this.rparams = new ArrayList<>();
    }

    public void addRparam(ArrayList<AddExpNode> as) {
        this.rparams.addAll(as);
    }

    @Override
    public void generateMidCode() {
        ArrayList<PushRpara> tempPush = new ArrayList<>();  //f(1, f(1,2))
        for (AddExpNode rp : rparams) {
            rp.generateMidCode();
            tempPush.add(new PushRpara(rp.getResult()));
        }
        for (PushRpara p : tempPush) {
            AllMidCodes.addMidCode(p);
        }
        AllMidCodes.addMidCode(new FunctionCall(ident));
        TempVar tempVar = new TempVar();
        BinaryExp binaryExp = new BinaryExp(tempVar, false);
        binaryExp.setOperand1(new RetValue(ident));
        this.result = tempVar;
        AllMidCodes.addMidCode(binaryExp);
    }

    @Override
    public Val getResult() {
        return this.result;
    }
}
