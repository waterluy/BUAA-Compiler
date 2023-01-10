package mid.midcodes;

import mid.midcodes.val.Ident;
import mid.midcodes.val.Val;

/**
 * var int i;
 * var int a = 1;
 * 变量 非数组
 */

public class VarDef implements MidCode {
    private final String logo;
    private final Ident ident;

    public VarDef(String type, Ident i) {
        this.logo = "var" + " " + type;
        this.ident = i;
    }

    @Override
    public String toString() {
        return logo + " " + ident.toString();
    }

    public Ident getIdent() {
        return ident;
    }
}
