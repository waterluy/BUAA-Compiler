package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelEqEnd extends Label implements MidCode {
    private static int index = 0;

    public LabelEqEnd() {
        index++;
        this.nowIndex = index;
        this.logo = "EqEnd";
    }
}
