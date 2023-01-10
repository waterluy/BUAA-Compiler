package mid.tree;

public class BlockStmtNode extends StmtNode implements TreeNode {
    private final BlockNode block;

    public BlockStmtNode(BlockNode block) {
        this.block = block;
    }

    @Override
    public void generateMidCode() {
        block.setLabelWhileBegin(labelWhileBegin);
        block.setLabelWhileEnd(labelWhileEnd);
        this.block.generateMidCode();
    }
}
