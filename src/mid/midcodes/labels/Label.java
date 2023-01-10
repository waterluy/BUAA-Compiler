package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class Label implements MidCode {
    protected String logo;
    protected Integer nowIndex;

    @Override
    public String toString() {
        return this.logo + this.nowIndex + ":";
    }

    public String getLabel() {
        return this.logo + this.nowIndex;
    }
}
