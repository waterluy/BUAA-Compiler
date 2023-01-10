package mid.midcodes;

import mid.midcodes.val.Val;

public class Printf implements MidCode {
    private final String logo;
    private final String s;
    private final Val val;
    private final boolean isStr;

    public Printf(String s, boolean isStr, Val v) {
        this.logo = "printf ";
        this.s = s;
        this.val = v;
        this.isStr = isStr;
    }

    @Override
    public String toString() {
        if (s != null) {
            return this.logo + this.s;
        } else {
            return this.logo + this.val.toString();
        }
    }

    public String getS() {
        return s;
    }

    public Val getVal() {
        return val;
    }
}
