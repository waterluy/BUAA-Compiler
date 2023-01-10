package mid.tree;

public class BTypeNode implements TreeNode {
    private final String type;

    public BTypeNode(String t) {
        this.type = t;
    }

    @Override
    public void generateMidCode() {

    }

    public String getType() {
        return type;
    }
}
