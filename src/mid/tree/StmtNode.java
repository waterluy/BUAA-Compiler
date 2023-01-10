package mid.tree;

import mid.midcodes.labels.LabelWhileBegin;
import mid.midcodes.labels.LabelWhileEnd;

public class StmtNode implements TreeNode, BlockItem {
    protected LabelWhileBegin labelWhileBegin = null;
    protected LabelWhileEnd labelWhileEnd = null;

    @Override
    public void generateMidCode() {

    }

    public void setLabelWhileBegin(LabelWhileBegin begin) {
        labelWhileBegin = begin;
    }

    public void setLabelWhileEnd(LabelWhileEnd end) {
        labelWhileEnd = end;
    }
}
