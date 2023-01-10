package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelAndEnd extends Label implements MidCode {
    private static int index = 0;

    public LabelAndEnd() {
        index++;
        this.nowIndex = index;
        this.logo = "AndEnd";
    }
}
