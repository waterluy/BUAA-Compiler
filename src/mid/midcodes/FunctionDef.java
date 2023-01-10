package mid.midcodes;

import java.util.ArrayList;

public class FunctionDef implements MidCode {
    private final String logo;
    private final String type;
    private final String name;
    private final boolean hasParam;
    private final ArrayList<FuncFpara> paras;   //TODO 好像没用？

    public FunctionDef(String t, String n, boolean hasa) {
        this.logo = "function def ";
        this.type = t;
        this.name = n;
        this.hasParam = hasa;   //有参数?
        this.paras = new ArrayList<>();
    }

    public void addParm(FuncFpara fp) {
        this.paras.add(fp);
    }

    public ArrayList<FuncFpara> getParas() {
        return paras;
    }

    @Override
    public String toString() {
        return this.logo + this.type + " " + this.name + "()";
    }

    public boolean isHasParam() {
        return hasParam;
    }

    public String getName() {
        return name;
    }
}
