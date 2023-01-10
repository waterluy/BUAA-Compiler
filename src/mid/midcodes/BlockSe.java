package mid.midcodes;

/**
 * block start
 * block end
 */

public class BlockSe implements MidCode {
    private static Integer index = 0;
    private final String logo = "Block";
    private final String se;
    private final Integer nowIndex;

    private final boolean isFunc;

    public BlockSe(String s, boolean f) {
        if (s.equals("start")) {
            index++;
        } else if (!s.equals("end")) {
            System.out.println("block start end index error");
        }
        this.nowIndex = index;
        this.se = s;
        this.isFunc = f;
    }

    @Override
    public String toString() {
        return this.logo + se + this.nowIndex;
    }

    public String getSe() {
        return se;
    }

    public boolean isFunc() {
        return isFunc;
    }
}
