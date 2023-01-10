package syntaxanalysis.symtable;

import java.util.ArrayList;

public class SymbolTable {
    private final ArrayList<VariableItem> varTable; //var
    private final ArrayList<FunctionItem> funcTable; //function
    private FunctionItem func; //记录该table属于哪个函数名

    public SymbolTable() { //基本块的符号表
        this.varTable = new ArrayList<>();
        this.funcTable = new ArrayList<>();
        this.func = null;
    }

    public SymbolTable(FunctionItem f) {   //函数的符号表
        this.varTable = new ArrayList<>();
        this.funcTable = new ArrayList<>();
        this.func = f;
    }

    public void addVar(VariableItem varItem) {
        varTable.add(varItem);
    }

    public void addFunc(FunctionItem funcItem) {
        funcTable.add(funcItem);
    }

    public boolean checkReDef(String name) {
        for (VariableItem v : varTable) {
            if (v.getName().equals(name)) {
                v.setReDef();
                return true;
            }
        }
        for (FunctionItem f : funcTable) {
            if (f.getName().equals(name)) {
                f.setReDef();
                return true;
            }
        }
        return false;
    }

    public FunctionItem getFunc() {
        return this.func;
    }

    public VariableItem checkNotDefVar(String name) {
        for (VariableItem v : varTable) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public FunctionItem checkNotDefFunc(String name) {
        for (FunctionItem f : funcTable) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    public boolean checkConstAssign(String name) {
        for (VariableItem v : varTable) {
            if (v.getName().equals(name) && v.isConst()) {
                return true;
            } else if (v.getName().equals(name)) {
                return false;
            }
        }
        return false;
    }
}
