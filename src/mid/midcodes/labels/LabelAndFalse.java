package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelAndFalse extends Label implements MidCode {
    private static int index = 0;

    public LabelAndFalse() {
        index++;
        this.nowIndex = index;
        this.logo = "AndFalse";
    }
}
