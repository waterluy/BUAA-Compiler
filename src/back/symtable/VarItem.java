package back.symtable;

import java.util.ArrayList;
import java.util.Objects;

public class VarItem extends ValItem {
    private final String name;
    private final boolean isConst;
    private final int dim;  //数组维度
    private ArrayList<Integer> values;  //数组的值  没用、、、
    private final ArrayList<Integer> dimLen;    //每维的长度
    private int valueNum;   //数组里面值的个数

    private boolean isParm = false; //默认不是参数

    public VarItem(String name, boolean isConst, int d, String s) {
        this.name = name;
        this.isConst = isConst;
        this.dim = d;
        this.values = new ArrayList<>();
        //this.values.add(null);  //如果不是数组，先初始化变量值为0
        this.stack = s;

        this.dimLen = new ArrayList<>();
        this.valueNum = 1;
        this.offset = -1;   //没分配内存
    }

    public void setParm() {
        isParm = true;
    }

    public boolean isParm() {
        return isParm;
    }

    public boolean isConst() {
        return isConst;
    }

    public void addValue(Integer value) {
        this.values.add(value);
        //this.values.set(i, value);
    }

    public Integer getValue(int i) {     //为null时, 表示编译器不知道值 可能是输入的数
        if (i >= values.size()) {
            return null;
        }
        return values.get(i);
    }

    public String getName() {
        return name;
    }

    public int getDim() {
        return dim;
    }

    public void setDimLen(Integer l) {
        this.dimLen.add(l);
        if ((!isParm) && (l != null)) {
            if (dimLen.size() == 1) {
                this.valueNum = l;
            } else if (dimLen.size() == 2) {
                if (dimLen.get(0) != null) {
                    this.valueNum = dimLen.get(0) * l;
                }
            }
        }
        /*
        if (dimLen.size() == 1) {           //数组初始化为null
            for (int i = 1; i < l; ++i) {   //因为加了初始值null，所以是从1开始不是从0开始
                this.values.add(0);
            }
            this.valueNum = l;
        } else if (dimLen.size() == 2) {    //数组初始化为null
            for (int i = 0; i < l * (dimLen.get(0) - 1); ++i) {     //因为添加第一维长度的时候初始化了第一行
                this.values.add(0);
            }
            this.valueNum = dimLen.get(0) * l;
        }*/
    }

    public int getValueNum() {
        return valueNum;
    }

    public int getDimLen(int d) {
        return dimLen.get(d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VarItem varItem = (VarItem) o;
        return Objects.equals(name, varItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
