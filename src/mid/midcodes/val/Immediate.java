package mid.midcodes.val;

/**
 * 立即数
 */

public class Immediate implements Val {
    private Integer value;

    public Immediate(Integer v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return value.toString();
    }
}
