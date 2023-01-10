package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.BlockSe;
import mid.midcodes.labels.LabelWhileBegin;
import mid.midcodes.labels.LabelWhileEnd;
import mid.midcodes.Return;

import java.util.ArrayList;

public class BlockNode implements TreeNode {
    private final ArrayList<BlockItem> blockItems;

    private LabelWhileBegin labelWhileBegin;
    private LabelWhileEnd labelWhileEnd;
    private boolean isVoidFunc;

    private boolean isFunc;

    public BlockNode() {
        this.blockItems = new ArrayList<>();
        this.isVoidFunc = false;
        this.isFunc = false;
    }

    public void addBlockItem(BlockItem bi) {
        if (bi != null) {
            this.blockItems.add(bi);
        }
    }

    public boolean isEmpty() {
        return blockItems.isEmpty();
    }

    @Override
    public void generateMidCode() {
        AllMidCodes.addMidCode(new BlockSe("start", this.isFunc));
        BlockSe blockEnd = new BlockSe("end", this.isFunc);
        if (!blockItems.isEmpty()) {
            for (BlockItem bi : blockItems) {
                if (bi instanceof StmtNode) {
                    ((StmtNode) bi).setLabelWhileBegin(labelWhileBegin);
                    ((StmtNode) bi).setLabelWhileEnd(labelWhileEnd);
                }
                bi.generateMidCode();
            }
            if (isVoidFunc && !(blockItems.get(blockItems.size() - 1) instanceof RetStmtNode)) {
                AllMidCodes.addMidCode(new Return());
            }
        }
        if (blockItems.isEmpty() && isVoidFunc) {
            AllMidCodes.addMidCode(new Return());
        }
        AllMidCodes.addMidCode(blockEnd);
    }

    public void setVoidFunc(boolean voidFunc) {
        isVoidFunc = voidFunc;
    }

    public void setLabelWhileBegin(LabelWhileBegin labelWhileBegin) {
        this.labelWhileBegin = labelWhileBegin;
    }

    public void setLabelWhileEnd(LabelWhileEnd labelWhileEnd) {
        this.labelWhileEnd = labelWhileEnd;
    }

    public void setFunc() {
        this.isFunc = true;
    }

    public boolean isFunc() {
        return isFunc;
    }
}
