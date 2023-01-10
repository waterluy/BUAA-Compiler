package mid.midcodes;

import mid.midcodes.val.Ident;
import mid.midcodes.val.Val;

/**
 * const int a = 1;
 * 常量 非数组
 */

public class ConstDef implements MidCode {
    private final String logo;
    private final Ident ident;
    private Val constInitVal;

    public ConstDef(String type, Ident i) {
        this.logo = "const" + " " + type;
        this.ident = i;
        this.constInitVal = null;
    }

    public void setConstInitVal(Val constInitVal) {
        this.constInitVal = constInitVal;   //一定不为null
    }

    @Override
    public String toString() {
        return logo + " " + ident.toString() + " = " + constInitVal.toString();
    }

    public Ident getIdent() {
        return ident;
    }

    public Val getConstInitVal() {
        return constInitVal;
    }
}
