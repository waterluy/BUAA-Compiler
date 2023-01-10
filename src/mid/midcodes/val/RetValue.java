package mid.midcodes.val;

/**
 * 函数返回值
 */

public class RetValue implements Val {
    private final String logo = "#RET";
    private final String func;

    public RetValue(String s) {
        this.func = s;
    }

    @Override
    public String toString() {
        return this.logo;
    }

    @Override
    public String getName() {
        return null;
    }
}
