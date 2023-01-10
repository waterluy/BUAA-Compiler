package mid;

public class CmpOp {

    private CmpOp() {

    }

    public static String getBranchOp(String o) {
        switch (o) {
            case "!=":
                return "sne";
            case "==":
                return "seq";
            case ">":
                return "sgt";
            case "<":
                return "slt";
            case ">=":
                return "sge";
            case "<=":
                return "sle";
            case "+":
                return "addu";
            case "-":
                return "subu";
            case "*":
                return "mul";
            case "/":
            case "%":
                return "div";
            default:
                System.out.println(o);
                return null;
        }
    }
}
