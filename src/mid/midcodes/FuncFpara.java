package mid.midcodes;

import mid.midcodes.val.Val;

public class FuncFpara implements MidCode {
    private final String logo;
    private final String type;
    private final String name;
    private final int dim;
    private Val len_2dim;

    public FuncFpara(String t, String n, int d) {
        this.logo = "para";
        this.type = t;
        this.name = n;
        this.dim = d;
    }

    public void setLen_2dim(Val len_2dim) {
        this.len_2dim = len_2dim;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(this.logo + " " + this.type + " " + this.name);
        if (this.dim == 1) {
            s.append("[]");
        } else if (this.dim == 2) {
            s.append("[]").append("[").append(len_2dim.toString()).append("]");
        }
        return s.toString();
    }

    public int getDim() {
        return dim;
    }

    public String getName() {
        return name;
    }

    public Val getLen_2dim() {
        return len_2dim;
    }
}
