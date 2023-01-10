package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelElse extends Label implements MidCode, Body {
    private static int index = 0;

    public LabelElse() {
        index++;
        this.nowIndex = index;
        this.logo = "Else";
    }
}
