package mid.midcodes;

import mid.midcodes.val.Val;

public class GetInt implements MidCode {
    private final String logo;
    private final Val lval;

    public GetInt(Val l) {
        this.logo = "scanf ";
        this.lval = l;
    }

    @Override
    public String toString() {
        return this.logo + this.lval.toString();
    }

    public Val getLval() {
        return lval;
    }
}
