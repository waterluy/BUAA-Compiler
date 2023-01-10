package back.symtable;

import mid.midcodes.val.TempVar;
import syntaxanalysis.symtable.FunctionItem;

import java.util.ArrayList;

public class SymTable {
    private final ArrayList<VarItem> varTable; //var
    private final ArrayList<FunctionItem> funcTable; //function
    private final ArrayList<TempVar> tempTable;
    private FuncItem func; //记录该table属于哪个函数名

    public SymTable() { //基本块的符号表
        this.varTable = new ArrayList<>();
        this.funcTable = new ArrayList<>();
        this.tempTable = new ArrayList<>();
        this.func = null;
    }

    public SymTable(FuncItem f) {   //函数的符号表
        this.varTable = new ArrayList<>();
        this.funcTable = new ArrayList<>();
        this.tempTable = new ArrayList<>();
        this.func = f;
    }

    public void addVarItem(VarItem v) {
        varTable.add(v);
    }

    public void addTempItem(TempVar t) {
        tempTable.add(t);
    }

    public VarItem findIdent(String n) {
        for (VarItem vi : varTable) {
            if (vi.getName().equals(n)) {
                return vi;
            }
        }
        return null;
    }

    public TempVar findTemp(String n) {
        for (TempVar t : tempTable) {
            if (t.toString().equals(n)) {   //tempVar 的name就是toString
                return t;
            }
        }
        return null;
    }

    public ArrayList<TempVar> getTempTable() {
        return tempTable;
    }

    public ArrayList<VarItem> getVarTable() {
        return varTable;
    }
}
