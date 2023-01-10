package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelIfEnd extends Label implements MidCode, Body {
    private static int index = 0;

    public LabelIfEnd() {
        index++;
        this.nowIndex = index;
        this.logo = "IfEnd";
    }
}
