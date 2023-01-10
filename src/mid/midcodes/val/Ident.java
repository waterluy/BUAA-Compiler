package mid.midcodes.val;

import java.util.ArrayList;

public class Ident implements Val {
    private final String name;
    private final Integer dim;      //定义时的数组维度，之后统一用一维数组保存
    private final ArrayList<Val> dimLens;   //每维的长度，只有一个的话表示一维数组或用一维保存

    public Ident(String s, Integer d) {
        this.name = s;
        this.dim = d;
        this.dimLens = new ArrayList<>();
    }

    public void addDimLen(Val l) {
        if (l != null) {
            this.dimLens.add(l);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(name);
        for (Val v : dimLens) {
            s.append("[").append(v.toString()).append("]");
        }
        return s.toString();
    }

    //mips
    public String getName() {
        return name;
    }

    public Integer getDim() {
        return dim;
    }

    public Val getDimLen(int d) {
        return dimLens.get(d);
    }
}
