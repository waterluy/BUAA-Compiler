package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelWhileBegin extends Label implements MidCode {
    private static int index = 0;

    public LabelWhileBegin() {
        index++;
        this.nowIndex = index;
        this.logo = "WhileBegin";
    }
}
