package syntaxanalysis.symtable;

import java.util.Objects;

public class VariableItem {
    private final String name;
    private final boolean isConst;
    private int dim;
    // int
    private boolean isReDef;

    public VariableItem(String name, boolean isConst) {
        this.dim = 0;
        this.isReDef = false;
        this.name = name;
        this.isConst = isConst;
    }

    public void addDim() {
        this.dim = this.dim + 1;
    }

    public String getName() {
        return name;
    }

    public void setReDef() {
        this.isReDef = true;
    }

    public boolean isConst() {
        return isConst;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VariableItem varItem = (VariableItem) o;
        return Objects.equals(name, varItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
