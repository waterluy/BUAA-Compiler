package mid.midcodes;

import mid.midcodes.val.Ident;

/**
 * 数组
 */

public class ArrayDef implements MidCode {
    private final boolean isConst;
    private final Ident ident;
    private final String logo;

    public ArrayDef(boolean c, String t, Ident i) {
        this.isConst = c;
        this.ident = i;
        if (isConst) {
            this.logo = "const arr " + t;
        } else {
            this.logo = "arr " + t;
        }
    }

    @Override
    public String toString() {
        return this.logo + " " + ident.toString();
    }

    public Ident getIdent() {
        return ident;
    }

    public boolean isConst() {
        return isConst;
    }
}
