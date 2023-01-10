package back;

import java.util.ArrayList;

public class RegMana {
    private static final ArrayList<Register> gpr = new ArrayList<Register>() {
        {
            add(new Register("$a1"));
            add(new Register("$a2"));
            add(new Register("$a3"));
            add(new Register("$v1"));
            add(new Register("$t0"));
            add(new Register("$t1") );
            add(new Register("$t2") );
            add(new Register("$t3") );
            add(new Register("$t4") );
            add(new Register("$t5") );
            add(new Register("$t6") );
            add(new Register("$t7") );
            add(new Register("$t8") );
            add(new Register("$t9") );
            add(new Register("$s0") );
            add(new Register("$s1") );
            add(new Register("$s2") );
            add(new Register("$s3") );
            add(new Register("$s4") );
            add(new Register("$s5") );
            add(new Register("$s6") );
            add(new Register("$s7") );
            add(new Register("$k0") );
            add(new Register("$k1") );
        }
    };
    
    private static final ArrayList<Register> savedReg = new ArrayList<Register>() {
        {
            addAll(gpr);    //24个
            add(new Register("$ra"));   //25个
            //不需要加 $a0 $v0        v0 只在返回值和syscall用
            //$zero $at $sp $gp $fp
        }
    };
    private static final ArrayList<Register> allRegs = new ArrayList<Register>() {
        {
            addAll(savedReg);
            add(new Register("$v0"));   //26
            add(new Register("$a0"));   //27
        }   //没有 $zero $at $fp $gp $sp
    };
    private static final int savedNum = savedReg.size();

    public RegMana() {

    }

    public static ArrayList<Register> getGpr() {
        return gpr;
    }

    public static Register str2reg(String s) {
        for (Register r : allRegs) {
            if (r.getName().equals(s)) {
                return r;
            }
        }
        return null;
    }

    public static Register getFreeReg() {
        for (Register r : gpr) {
            if (r.isFree() && !r.isConflict()) {
                return r;
            }
        }
        for (Register r : gpr) {
            if (!r.isConflict()) {
                return r;
            }
        }
        System.out.println("aaaaaaaaaaaaaa不可能");
        return null;
    }

    public static ArrayList<Register> getSavedReg() {
        return savedReg;
    }

    public static int getSavedNum() {
        return savedNum;
    }
}
