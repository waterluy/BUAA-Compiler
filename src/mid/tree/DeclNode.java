package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.ArrayDef;
import mid.midcodes.BinaryExp;
import mid.midcodes.ConstDef;
import mid.midcodes.VarDef;
import mid.midcodes.val.Ident;
import mid.midcodes.val.Immediate;
import mid.midcodes.val.Val;

import java.util.ArrayList;

public class DeclNode implements TreeNode, BlockItem {
    private BTypeNode bType;
    private final ArrayList<DefNode> defs;
    private final boolean isConst;

    public DeclNode(boolean isConst, BTypeNode t) {
        this.bType = t;
        this.defs = new ArrayList<>();
        this.isConst = isConst;
    }

    public void addDef(DefNode def) {
        this.defs.add(def);
    }

    @Override
    public void generateMidCode() {
        for (DefNode d : defs) {
            d.generateMidCode();
            if (d.getDim() == 0) {  //不是数组
                if (isConst) {
                    ConstDef constDef = new ConstDef(bType.getType(), d.getMidIdent());
                    constDef.setConstInitVal(d.getInitVal());
                    AllMidCodes.addMidCode(constDef);
                } else {
                    VarDef varDef = new VarDef(bType.getType(), d.getMidIdent());
                    //varDef.setInitVal(d.getInitVal());
                    Val init = d.getInitVal();
                    AllMidCodes.addMidCode(varDef);
                    if (init != null) {
                        BinaryExp b = new BinaryExp(d.getMidIdent(), false);
                        b.setOperand1(init);
                        AllMidCodes.addMidCode(b);
                    }
                }
            } else {    //数组
                ArrayDef arrayDef = new ArrayDef(isConst, bType.getType(), d.getMidIdent());
                AllMidCodes.addMidCode(arrayDef);
                int i = 0;
                Val iv;
                while ((iv = d.getInitVal()) != null) {
                    Ident result = new Ident(d.getIdent(), 1); //赋值语句，按1维记录
                    result.addDimLen(new Immediate(i)); //都按一维数组赋值 只有定义时展平
                    BinaryExp binaryExp = new BinaryExp(result, isConst);
                    binaryExp.setOperand1(iv);
                    AllMidCodes.addMidCode(binaryExp);
                    ++i;
                }
            }
        }
    }
}
