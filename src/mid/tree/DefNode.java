package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.val.Ident;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class DefNode implements TreeNode {
    private final String ident;
    private final ArrayList<AddExpNode> arrayDim; //ConstExp->AddExp  size为数组位维数，exp为数组每维对应长度
    private InitValNode initVal; //ConstExp->AddExp(const)  Exp->AddExp

    private Ident midIdent;
    private final boolean isConst;

    public DefNode(String i, boolean isConst) {
        this.ident = i;
        this.arrayDim = new ArrayList<>();
        this.initVal = null;
        this.midIdent = null;
        this.isConst = isConst;
    }

    public void addDim(AddExpNode addExp) {
        addExp.setForConst(true);   //数组维度 ConstExp
        this.arrayDim.add(addExp);
    }

    public void setInitVaL(InitValNode c) {
        this.initVal = c;
        this.initVal.setForConst(isConst);
    }

    @Override
    public void generateMidCode() {
        this.midIdent = new Ident(ident, getDim());
        for (AddExpNode addExp : arrayDim) {    //最多2维
            addExp.generateMidCode();
            if ((addExp.getResult() instanceof Ident) && (((Ident) addExp.getResult()).getDim() != 0)) {
                TempVar t = new TempVar();
                BinaryExp b = new BinaryExp(t, true);
                b.setOperand1(addExp.getResult());      //数组套数组 a[b[1]] 加一步#t = b[1]
                AllMidCodes.addMidCode(b);
                this.midIdent.addDimLen(t);
            } else {
                this.midIdent.addDimLen(addExp.getResult());
            }
        }
    }

    public Integer getDim() {
        return arrayDim.size();
    }

    public Ident getMidIdent() {
        return this.midIdent;
    }

    public Val getInitVal() {
        if (initVal != null) {
            return initVal.getVal();
        } else {
            return null;
        }
    }

    public String getIdent() {
        return ident;
    }
}
