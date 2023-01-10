package mid.midcodes.labels;

import mid.midcodes.MidCode;

public class LabelRelEnd extends Label implements MidCode {
    private static int index = 0;

    public LabelRelEnd() {
        index++;
        this.nowIndex = index;
        this.logo = "RelEnd";
    }
}
