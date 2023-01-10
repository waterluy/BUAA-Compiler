package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelWhileBody extends Label implements MidCode, Body {
    private static int index = 0;

    public LabelWhileBody() {
        index++;
        this.nowIndex = index;
        this.logo = "WhileBody";
    }
}
