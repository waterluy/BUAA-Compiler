package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelIfBody extends Label implements MidCode, Body {
    private static int index = 0;

    public LabelIfBody() {
        index++;
        this.nowIndex = index;
        this.logo = "IfBody";
    }
}
