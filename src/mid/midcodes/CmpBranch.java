package mid.midcodes;

import mid.midcodes.val.Val;

public class CmpBranch implements MidCode {
    private final String cmpOp;
    private Val operand1;
    private Val operand2;
    private String label;

    public CmpBranch(String op) {
        this.cmpOp = op;
    }

    public void setOperand1(Val operand1) {
        this.operand1 = operand1;
    }

    public void setOperand2(Val operand2) {
        this.operand2 = operand2;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return cmpOp + " " + operand1.toString() + ", " + operand2.toString() + ", " + label;
    }

    public String getCmpOp() {
        return cmpOp;
    }

    public String getLabel() {
        return label;
    }

    public Val getOperand2() {
        return operand2;
    }

    public Val getOperand1() {
        return operand1;
    }
}
