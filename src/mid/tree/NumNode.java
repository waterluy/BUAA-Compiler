package mid.tree;

import mid.midcodes.val.Immediate;
import mid.midcodes.val.Val;

public class NumNode implements TreeNode, PrimaryExpNode {
    private final Integer num;

    public NumNode(Integer n) {
        this.num = n;
    }

    @Override
    public void generateMidCode() {

    }

    @Override
    public Val getResult() {
        return new Immediate(num);
    }
}
