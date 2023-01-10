package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelEqFalse extends Label implements MidCode {
    private static int index = 0;

    public LabelEqFalse() {
        index++;
        this.nowIndex = index;
        this.logo = "EqFalse";
    }
}
