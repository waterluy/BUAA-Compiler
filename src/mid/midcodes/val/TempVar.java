package mid.midcodes.val;

import back.symtable.ValItem;

import java.util.Objects;

/**
 * 生成临时变量
 */

public class TempVar extends ValItem implements Val {
    private static int index = 0;
    private final String logo = "#t";
    private final int nowIndex;

    public TempVar() {
        index++;
        this.nowIndex = index;
        this.value = null;
        //mips
        this.stack = "$sp";
    }

    @Override
    public String toString() {
        return logo + this.nowIndex;
    }

    @Override
    public String getName() {
        return logo + this.nowIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TempVar tempVar = (TempVar) o;
        return nowIndex == tempVar.nowIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nowIndex);
    }
}
