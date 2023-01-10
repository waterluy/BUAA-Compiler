package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.ConstStr;
import mid.midcodes.Printf;

import java.util.ArrayList;

public class PrintfStmtNode extends StmtNode implements TreeNode {
    private String formatStr;
    private final ArrayList<AddExpNode> exps;

    public PrintfStmtNode() {
        this.exps = new ArrayList<>();
    }

    public void setFormatStr(String s) {
        this.formatStr = s;
    }

    public void addExp(AddExpNode a) {
        this.exps.add(a);
    }

    @Override
    public void generateMidCode() {
        int sl = ConstStr.getIndex(); //开始的str index
        String[] split = formatStr.replaceAll("\"", "").split("%d");
        for (String s : split) {
            if (!s.equals("")) {
                AllMidCodes.addMidCode(new ConstStr(s));
            }
        }
        String str = formatStr.replaceAll("\"", "");
        ArrayList<Printf> tempRrintf = new ArrayList<>();   //对于printf("xx%dxx\n", f()) 要先计算f()
        int i = 0;
        while (true) {
            if (str.equals("")) {
                break;
            }
            int pos = str.indexOf("%d");
            if (pos == -1) {
                tempRrintf.add(new Printf("constStr" + sl, true, null));
                break;
            }
            if (pos == 0) {
                AddExpNode a = exps.get(i);
                a.generateMidCode();
                tempRrintf.add(new Printf(null, false, a.getResult()));
                str = str.substring(2);
                i++;
            } else {
                tempRrintf.add(new Printf("constStr" + sl, true, null));
                sl++;
                str = str.substring(pos);
            }
        }
        for (Printf p : tempRrintf) {
            AllMidCodes.addMidCode(p);
        }

    }
}
