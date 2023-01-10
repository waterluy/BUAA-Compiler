package mid.midcodes;

import mid.midcodes.val.Val;

public class BinaryExp implements MidCode {
    private final Val result;
    private Val operand1;
    private String op;
    private Val operand2;
    private boolean forConst;

    public BinaryExp(Val r, boolean forConst) {
        this.result = r;
        this.operand1 = null;
        this.op = null;
        this.operand2 = null;
        this.forConst = forConst;
    }

    public boolean isForConst() {
        return forConst;
    }

    public void setOperand1(Val operand1) {
        this.operand1 = operand1;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setOperand2(Val operand2) {
        this.operand2 = operand2;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(result.toString() + " =");
        if (operand1 != null) {
            s.append(" ").append(operand1.toString());
        }
        if (op != null) {
            s.append(" ").append(op);
        }
        if (operand2 != null) {
            s.append(" ").append(operand2.toString());
        }
        if (forConst) {
            s.append(" FOR CONST");
        }
        return s.toString();
    }

    public Val getResult() {
        return result;
    }

    public String getOp() {
        return op;
    }

    public Val getOperand1() {
        return operand1;
    }

    public Val getOperand2() {
        return operand2;
    }
}
