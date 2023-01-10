package syntaxanalysis.symtable;

import java.util.ArrayList;

public class FunctionItem {
    private final String name;
    private final ReturnType returnType;
    private boolean isReDef;
    //参数
    private final ArrayList<VariableItem> fparas;
    private final ArrayList<Integer> dims;

    public FunctionItem(String name, String type) {
        this.name = name;
        if (type.equals("int")) {
            this.returnType = ReturnType.INT;
        } else {
            this.returnType = ReturnType.VOID;
        }
        this.isReDef = false;
        this.fparas = new ArrayList<>();
        this.dims = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addPara(VariableItem varItem) {
        fparas.add(varItem);
        dims.add(varItem.getDim());
    }

    public void setReDef() {
        this.isReDef = true;
    }

    public int getFparaNum() {
        return fparas.size();
    }

    public ArrayList<Integer> getDims() {
        return dims;
    }

    public ReturnType getReturnType() {
        return returnType;
    }
}
