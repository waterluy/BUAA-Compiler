package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BinaryExp;
import mid.midcodes.val.Ident;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class LValNode implements TreeNode, PrimaryExpNode {
    private final String ident;
    private int dim;
    private final ArrayList<AddExpNode> arrayDim;

    private Ident lval;
    private boolean forConst;

    public LValNode(String i) {
        this.ident = i;
        this.dim = 0;
        this.arrayDim = new ArrayList<>();
    }

    public void setForConst(boolean forConst) {
        this.forConst = forConst;
    }

    public boolean isForConst() {
        return forConst;
    }

    public void addDim(AddExpNode a) {
        this.arrayDim.add(a);
        this.dim++;
    }

    @Override
    public void generateMidCode() {
        this.lval = new Ident(ident, dim);
        if (dim == 1) {
            AddExpNode d1 = arrayDim.get(0);
            d1.generateMidCode();
            if ((d1.getResult() instanceof Ident) && (((Ident) d1.getResult()).getDim() != 0)) {
                TempVar t = new TempVar();
                BinaryExp b = new BinaryExp(t, forConst);
                b.setOperand1(d1.getResult());      //数组套数组 a[b[1]] 加一步 #t = b[1] a[#t]
                AllMidCodes.addMidCode(b);
                this.lval.addDimLen(t);
            } else {
                this.lval.addDimLen(d1.getResult());
            }
        } else if (dim == 2) {
            AddExpNode d1 = arrayDim.get(0);
            d1.generateMidCode();
            AddExpNode d2 = arrayDim.get(1);
            d2.generateMidCode();
            if ((d1.getResult() instanceof Ident) && (((Ident) d1.getResult()).getDim() != 0)) {
                TempVar t = new TempVar();
                BinaryExp b = new BinaryExp(t, forConst);
                b.setOperand1(d1.getResult());      //数组套数组 a[b[1]] 加一步 #t = b[1] a[#t]
                AllMidCodes.addMidCode(b);
                this.lval.addDimLen(t);
            } else {
                this.lval.addDimLen(d1.getResult());
            }
            if ((d2.getResult() instanceof Ident) && (((Ident) d2.getResult()).getDim() != 0)) {
                TempVar t = new TempVar();
                BinaryExp b = new BinaryExp(t, forConst);
                b.setOperand1(d2.getResult());      //数组套数组 a[b[1]] 加一步 #t = b[1] a[#t]
                AllMidCodes.addMidCode(b);
                this.lval.addDimLen(t);
            } else {
                this.lval.addDimLen(d2.getResult());
            }
        }
    }

    public Val getLval() {
        return this.lval;
    }

    @Override
    public Val getResult() {
        return this.lval;
    }
}
