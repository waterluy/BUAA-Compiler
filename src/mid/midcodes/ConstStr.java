package mid.midcodes;

public class ConstStr implements MidCode {
    private final String logo;
    private final String str;
    private static Integer index = 0;
    private final Integer nowIndex;

    public ConstStr(String s) {
        this.nowIndex = index;
        this.logo = "const str constStr" + this.nowIndex;
        index++;
        this.str = "\"" + s + "\"";
    }

    @Override
    public String toString() {
        return this.logo + " " + this.str;
    }

    public static Integer getIndex() {
        return index;
    }

    public String getStr() {
        return str;
    }

    public String getName() {
        return "constStr" + this.nowIndex;
    }
}
