package mid.tree;

import back.symtable.SymTable;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class InitValNode implements TreeNode {
    private final ArrayList<AddExpNode> initVals;
    private int index;
    private Val nowInitVal;

    private boolean forConst;

    public InitValNode() {
        this.initVals = new ArrayList<>();
        this.index = -1;
        this.nowInitVal = null;
    }

    public boolean isForConst() {
        return forConst;
    }

    public void setForConst(boolean forConst) {
        this.forConst = forConst;
    }

    public void addInitVal(ArrayList<AddExpNode> a) {
        this.initVals.addAll(a);
    }

    @Override
    public void generateMidCode() {
        AddExpNode addExpNode = initVals.get(index);
        addExpNode.setForConst(forConst);
        addExpNode.generateMidCode();
        this.nowInitVal = addExpNode.getResult();
    }

    public Val getVal() {
        this.index++;
        if (this.index >= initVals.size()) {
            return null;
        }
        generateMidCode();
        return this.nowInitVal;
    }
}
