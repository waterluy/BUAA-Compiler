package mid.midcodes;

import mid.midcodes.val.Val;

public class PushRpara implements MidCode {
    private final String logo;
    private final Val param;

    public PushRpara(Val rp) {
        this.logo = "push ";
        this.param = rp;
    }

    @Override
    public String toString() {
        return this.logo + " " + this.param.toString();
    }

    public Val getParam() {
        return param;
    }
}
