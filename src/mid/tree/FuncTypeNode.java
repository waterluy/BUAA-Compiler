package mid.tree;

public class FuncTypeNode implements TreeNode {
    private final String type;

    public FuncTypeNode(String t) {
        this.type = t;
    }

    @Override
    public void generateMidCode() {

    }

    public String getType() {
        return type;
    }
}
