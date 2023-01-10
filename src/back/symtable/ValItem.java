package back.symtable;

import back.Register;

public class ValItem {
    protected Integer value = null;
    protected Register reg = null;
    protected String stack; //存放在哪个栈 "fp" or "gp"
    protected int offset;

    public int getOffset() {
        if (offset == -1) {
            if (this instanceof VarItem) {
                System.out.println(((VarItem) this).getName() + " hhhhhh--");
            }
        }
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getStack() {
        return stack;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setReg(Register reg) {
        this.reg = reg;
    }

    public Register getReg() {
        return reg;
    }

    public String getName() {
        return null;
    }
}
