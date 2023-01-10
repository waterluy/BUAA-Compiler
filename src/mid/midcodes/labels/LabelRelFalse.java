package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelRelFalse extends Label implements MidCode {
    private static int index = 0;

    public LabelRelFalse() {
        index++;
        this.nowIndex = index;
        this.logo = "RelFalse";
    }
}
