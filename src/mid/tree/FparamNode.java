package mid.tree;

import mid.midcodes.FuncFpara;

public class FparamNode implements TreeNode {
    private final BTypeNode type;
    private final String name;
    private Integer dim; //定义时的形参维度
    private AddExpNode len_2dim; //数组的第二维长度

    private FuncFpara midCode;

    public FparamNode(BTypeNode t, String n) {
        this.type = t;
        this.name = n;
        this.dim = 0;
        this.len_2dim = null;
    }

    public void addDim() {
        this.dim = this.dim + 1;
    }

    public void setLen_2dim(AddExpNode len) {
        this.len_2dim = len;
    }

    @Override
    public void generateMidCode() {
        this.midCode = new FuncFpara(this.type.getType(), this.name, this.dim);
        if (len_2dim != null) {
            len_2dim.setForConst(true); //bug !!!!数组维度都要是常数 不要忘记函数参数
            len_2dim.generateMidCode();
            this.midCode.setLen_2dim(len_2dim.getResult());
        }
    }

    public FuncFpara getMidCode() {
        return midCode;
    }
}
