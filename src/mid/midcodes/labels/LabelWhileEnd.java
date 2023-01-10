package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelWhileEnd extends Label implements MidCode, Body {
    private static int index = 0;

    public LabelWhileEnd() {
        index++;
        this.nowIndex = index;
        this.logo = "WhileEnd";
    }
}
