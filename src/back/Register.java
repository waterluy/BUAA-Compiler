package back;

import back.symtable.ValItem;

import java.util.Objects;

public class Register {
    private final String name;
    private boolean isConflict;  //统一表达式中的两操作数的寄存器冲突 a[b]数组地址和偏移两寄存器冲突
    private boolean isFree;      //寄存器对应一个变量
    private ValItem item;

    public Register(String n) {
        this.name = n;
        this.isConflict = false;
        this.isFree = true;
        this.item = null;
    }

    public ValItem getItem() {
        return item;
    }

    public void setItem(ValItem item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public boolean isConflict() {
        return isConflict;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setConflict(boolean conflict) {
        isConflict = conflict;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Register register = (Register) o;
        return Objects.equals(name, register.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
