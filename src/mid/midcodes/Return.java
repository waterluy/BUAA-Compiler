package mid.midcodes;

import mid.midcodes.val.Val;

public class Return implements MidCode {
    private final String logo;
    private final Val retExp;

    public Return() {
        this.logo = "return";
        this.retExp = null;
    }

    public Return(Val r) {
        this.logo = "return ";
        this.retExp = r;
    }

    @Override
    public String toString() {
        if (retExp != null) {
            return this.logo + this.retExp.toString();
        } else {
            return this.logo;
        }
    }

    public Val getRetExp() {
        return retExp;
    }
}
