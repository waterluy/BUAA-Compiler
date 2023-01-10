package back;

import back.symtable.SymTable;
import back.symtable.ValItem;
import back.symtable.VarItem;
import mid.CmpOp;
import mid.midcodes.ArrayDef;
import mid.midcodes.BinaryExp;
import mid.midcodes.BlockSe;
import mid.midcodes.CmpBranch;
import mid.midcodes.ConstDef;
import mid.midcodes.ConstStr;
import mid.midcodes.FuncFpara;
import mid.midcodes.FunctionCall;
import mid.midcodes.FunctionDef;
import mid.midcodes.GetInt;
import mid.midcodes.Goto;
import mid.midcodes.MidCode;
import mid.midcodes.Printf;
import mid.midcodes.PushRpara;
import mid.midcodes.Return;
import mid.midcodes.VarDef;
import mid.midcodes.labels.Label;
import mid.midcodes.val.Ident;
import mid.midcodes.val.Immediate;
import mid.midcodes.val.RetValue;
import mid.midcodes.val.TempVar;
import mid.midcodes.val.Val;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 生成 mips 代码
 */

public class MidToMips {
    private final ArrayList<MidCode> midCodes;
    private final ArrayList<ConstStr> constStrs;
    private final ArrayList<String> mips;
    private int index = 0;
    private final int num;  //midCodes总数量

    private final Stack<SymTable> tables = new Stack<>();
    private final Stack<Integer> fps = new Stack<>();
    private SymTable nowTable;

    private int nowOffset = 0; //gp fp offset 相对fp
    private int nowFp; //fp 相对栈底
    private int tempOffset = 0; //临时变量存在 sp,

    private int fparaNum = -1;  //第几个形参
    private int rparaNum = -1;  //第几个实参
    private boolean saved = false;

    public MidToMips(ArrayList<MidCode> m, ArrayList<ConstStr> c) {
        this.midCodes = m;
        this.num = m.size();
        this.constStrs = c;
        this.mips = new ArrayList<>();
    }

    public void toMips() {
        mips.add(".data");
        mipsConstStr(); //字符串常量存到.data .asciiz
        mips.add(".text");
        mipsGlobal();   //处理全局变量和字符串常量 栈为gp
        mips.add("li $fp, 0x10040000");
        nowFp = 0x10040000;
        nowOffset = 0;  //栈为fp
        mips.add("jal func_main");
        addSyscall(10);
        mips.add("\n");
        nowTable = new SymTable();  //main符号表
        tables.push(nowTable);
        for (; index < num; ++index) {
            MidCode m = midCodes.get(index);
            if (m instanceof ConstDef) {
                constDef((ConstDef) m, "$fp");
            } else if (m instanceof VarDef) {
                varDef((VarDef) m, "$fp");
            } else if (m instanceof ArrayDef) {
                arrayDef((ArrayDef) m, "$fp");
            } else if (m instanceof FunctionDef) {
                funcDef((FunctionDef) m);
            } else if (m instanceof FuncFpara) {
                funcFpara((FuncFpara) m);
            } else if (m instanceof BinaryExp) {
                binaryExp((BinaryExp) m);
            } else if (m instanceof GetInt) {
                getInt((GetInt) m);
            } else if (m instanceof Printf) {
                printf((Printf) m);
            } else if (m instanceof PushRpara) {
                pushRpara((PushRpara) m);
            } else if (m instanceof FunctionCall) {
                funcCall((FunctionCall) m);
            } else if (m instanceof Return) {
                returnCode((Return) m);
            } else if (m instanceof CmpBranch) {
                addComment(" # 基本块分界线");
                blockClearReg();
                cmpCode((CmpBranch) m);
            } else if (m instanceof Goto) {
                addComment(" # 基本块分界线");
                blockClearReg();
                mips.add("j " + ((Goto) m).getLabel());
            } else if (m instanceof Label) {
                mips.add("\n");
                blockClearReg();    //要加在label之前！！！
                mips.add(((Label) m).getLabel() + ":");
            } else if (m instanceof BlockSe) {
                blockSe((BlockSe) m);
            }
        }
    }

    /**
     * 两个相邻的基本块之间要把寄存器里存的值都写回对应的内存
     */
    private void blockClearReg() {
        for (Register r : RegMana.getGpr()) {
            if (r.getItem() != null) {
                writeRegToMem(null, r.getItem(), r);
                r.getItem().setReg(null);
                r.getItem().setReg(null);
                r.setItem(null);
            }
            r.setFree(true);
        }
    }

    public void mipsConstStr() {             //字符串常量存到.data .asciiz
        for (ConstStr cs : constStrs) {
            mips.add(cs.getName() + ": .asciiz " + cs.getStr());
        }
        mips.add("\n");
    }

    public void mipsGlobal() {
        nowTable = new SymTable();
        tables.push(nowTable);        //全局符号表 最外层的
        for (; index < num; ++index) {     //全局变量存到 gp 栈
            MidCode m = midCodes.get(index);
            if (m instanceof ConstDef) {
                constDef((ConstDef) m, "$gp");
            } else if (m instanceof VarDef) {
                varDef((VarDef) m, "$gp");
            } else if (m instanceof ArrayDef) {
                arrayDef((ArrayDef) m, "$gp");
            } else if (m instanceof BinaryExp) {    //数组全部都要存到内存里 a[b]
                binaryExp((BinaryExp) m);
            } else {
                break;
            }
        }
        mips.add("\n");
    }

    private void setOffset(VarItem v, int space) {
        v.setOffset(nowOffset);
        nowOffset += space;
    }

    private void setTempOffset(TempVar t) {
        t.setOffset(tempOffset);
        tempOffset -= 4;
    }

    //只在constExp里调用
    public Integer getConstExp(Val constExp) {      //如果符号表已知值,则返回数值,否则返回null
        if (constExp instanceof Ident) {   //找到常量初值 now 总共只有一层符号表
            VarItem vi = findVarItem(constExp.getName());
            assert vi != null;
            if (!vi.isConst()) {    //只能取常量的值
                return null;
            }
            int d = ((Ident) constExp).getDim();
            if (d == 0) {   //只是一个变量
                return vi.getValue();
            } else if (d == 1) {    //一维数组
                Val d1 = ((Ident) constExp).getDimLen(0);
                Integer i = getConstExp(d1);
                if (i != null) {
                    return vi.getValue(i);  //bug注意数组！！必须是get(i)
                }
            } else if (d == 2) {    //二维数组
                Val d1 = ((Ident) constExp).getDimLen(0);   //使用的行
                Val d2 = ((Ident) constExp).getDimLen(1);   //使用的列
                int lie = vi.getDimLen(1);     //得到第二维长度 即有多少列
                Integer i1 = getConstExp(d1);
                Integer i2 = getConstExp(d2);
                if ((i1 != null) && (i2 != null)) {
                    int index = i1 * lie + i2;
                    return vi.getValue(index);
                }
            }
        } else if (constExp instanceof TempVar) {
            return ((TempVar) constExp).getValue();
        } else if (constExp instanceof Immediate) {
            return ((Immediate) constExp).getValue();
        }
        return null;
    }

    private void constDef(ConstDef c, String stack) {   //stack : $sp or $gp
        Ident cvDef = c.getIdent();    //常量定义 非数组
        VarItem varItem = new VarItem(cvDef.getName(), true, cvDef.getDim(), stack);   //dim=0
        Val constInit = c.getConstInitVal();
        //Integer t = getConstExp(constInit);
        //System.out.println(t);
        varItem.setValue(getConstExp(constInit));   //找到初值，必是常量
        //setOffset(varItem); 常量为初始定值, 保存在符号表, 不在内存里存了
        nowTable.addVarItem(varItem);
    }

    private void varDef(VarDef v, String stack) {
        Ident varDef = v.getIdent();
        VarItem varItem = new VarItem(varDef.getName(), false, varDef.getDim(), stack);
        setOffset(varItem, 4);     //在内存中分配位置
        nowTable.addVarItem(varItem);
    }

    private void arrayDef(ArrayDef a, String stack) {
        Ident arr = a.getIdent();
        VarItem varItem = new VarItem(arr.getName(), a.isConst(), arr.getDim(), stack);
        for (int i = 0; i < arr.getDim(); ++i) {
            Val dl = arr.getDimLen(i);
            //System.out.println(varItem.getName() + " " + getConstExp(dl));
            varItem.setDimLen(getConstExp(dl));     //设置每维的长度，必已知
        }
        //System.out.println(varItem.getValueNum());
        setOffset(varItem, 4 * varItem.getValueNum());
        nowTable.addVarItem(varItem);
    }

    private Register getReg() {
        Register reg = RegMana.getFreeReg();  //寄存器里不保存数组的值
        assert reg != null;
        if ((reg.getItem() != null)) {
            if ((reg.getItem() instanceof VarItem) && (((VarItem) reg.getItem()).isConst())) {
                reg.getItem().setReg(null);  //不需要写回内存
            } else {    //寄存器里不保存数组的值
                addComment("寄存器不够 写回内存 " + reg.getItem().getName() + " " + reg.getItem().getReg().getName() + " " + reg.getItem().getName());
                writeRegToMem(null, reg.getItem(), reg);    // 把寄存器里的值写回内存
                reg.getItem().setReg(null);
            }
            reg.setItem(null);
            reg.setFree(true);
        }
        return reg;
    }

    private void binaryExp(BinaryExp b) {        //数组只能直接操作内存  a[b]
        Val result = b.getResult();
        Integer resValue = null;
        ValItem resItem = null;
        if (result instanceof Ident) {
            resItem = findVarItem(result.getName());
        } else if (result instanceof TempVar) {
            resItem = findTemp(result.getName());
            if (resItem == null) {
                resItem = (TempVar) result;
                setTempOffset((TempVar) result);
                nowTable.addTempItem((TempVar) result);
            }
        }
        assert resItem != null;
        Register resReg = resItem.getReg(); //寄存器里可能保存了0维数的值
        if (resReg == null) {
            resReg = getReg();
        }
        resReg.setFree(false);
        resReg.setConflict(true);
        //
        Val op1 = b.getOperand1();
        String op = b.getOp();
        Val op2 = b.getOperand2();
        Integer i1;     //第一个操作数的数值
        Integer i2;     //第二个操作数的数值
        if (op1 == null) {          //res = -op2
            if ((i1 = getOpValue(op2)) != null) {    //符号表里能直接得到值
                resValue = calculate(0, op, i1);
                addLi(resReg.getName(), resValue);
            } else {        //操作内存/寄存器, 为这个op2分配寄存器
                Register op2reg = getOperandReg(op2);
                assert op2reg != null;
                switch (op) {
                    case "-":
                        add3op("subu", resReg.getName(), "$zero", op2reg.getName());
                        break;
                    case "!":
                        add3op("seq", resReg.getName(), op2reg.getName(), "$zero");
                        break;
                    default:    // + 啥也不做
                        addMove(resReg.getName(), op2reg.getName());
                        break;
                }
            }
        } else if (op2 == null) {   //res = op1
            if ((i1 = getOpValue(op1)) != null) {
                resValue = i1;
                addLi(resReg.getName(), resValue);
            } else {
                Register op1reg = getOperandReg(op1);
                assert op1reg != null;
                addMove(resReg.getName(), op1reg.getName());
            }
        } else {                    //res = op1 + op2
            i1 = getOpValue(op1);
            i2 = getOpValue(op2);
            if ((i1 != null) && (i2 != null)) {
                resValue = calculate(i1, op, i2);
                addLi(resReg.getName(), resValue);
            } else if ((i1 == null) && (i2 != null)) {
                Register op1reg = getOperandReg(op1);
                assert op1reg != null;
                switch (op) {
                    case "<":
                        addLi(resReg.getName(), i2);
                        add3op("slt", resReg.getName(), op1reg.getName(), resReg.getName());
                        break;
                    default:
                        add3op(CmpOp.getBranchOp(op), resReg.getName(), op1reg.getName(), String.valueOf(i2));
                        if (op.equals("%")) {
                            mips.add("mfhi " + resReg.getName());   //取余数
                        }
                        break;
                }
            } else if (i1 != null) {    //i2 == null
                Register op2reg = getOperandReg(op2);
                assert op2reg != null;
                switch (op) {
                    case "+":
                        add3op("addu", resReg.getName(), op2reg.getName(), String.valueOf(i1));
                        break;
                    case "*":
                        add3op("mul", resReg.getName(), op2reg.getName(), String.valueOf(i1));
                        break;
                    default:    // i1 - / % rel eq
                        addLi(resReg.getName(), i1);
                        add3op(CmpOp.getBranchOp(op), resReg.getName(), resReg.getName(), op2reg.getName());
                        if (op.equals("%")) {
                            mips.add("mfhi " + resReg.getName());   //取余数
                        }
                        break;
                }
            } else {    //两操作数都为null
                Register op1reg = getOperandReg(op1);
                assert op1reg != null;
                op1reg.setConflict(true);
                Register op2reg = getOperandReg(op2);
                op1reg.setConflict(false);
                assert op2reg != null;
                add3op(CmpOp.getBranchOp(op), resReg.getName(), op1reg.getName(), op2reg.getName());
                if (op.equals("%")) {
                    mips.add("mfhi " + resReg.getName());   //取余数
                }
            }
        }
        resReg.setConflict(false);  //!!!!! setConflict true false必须成对出现
        for (Register gpr : RegMana.getGpr()) {                                   //bug 要用==不能用.equals会误删
            if ((gpr.getItem() instanceof VarItem) && (gpr.getItem() != null) && (gpr.getItem()) == resItem) {
                gpr.setItem(null);
                gpr.setFree(true);
            }   //bug j = next[j] 冲突，在找next[j]的时候给j分配了一个寄存器，现在释放
        }
        //bug ！！ 要计算完之后再resItem.setReg 否则res=res语句会以为res有了寄存器而出错
        resReg.setItem(resItem);
        resReg.setFree(false);
        resItem.setReg(resReg);
        addComment("先把 " + result.getName() + " 的值保存进寄存器 " + resReg.getName() + " 里");
        if (b.isForConst()) {   //为了计算常数要填 !符号表!
            assert resValue != null;
            if (result instanceof Ident) {
                int d = ((Ident) result).getDim();
                if (d == 0) {   //只是一个变量
                    resItem.setValue(resValue);
                } else if (d == 1) {    //一维数组
                    Val d1 = ((Ident) result).getDimLen(0);
                    //System.out.println(resItem.getName() + " " + resValue);
                    ((VarItem) resItem).addValue(resValue);
                } else if (d == 2) {    //二维数组
                    Val d1 = ((Ident) result).getDimLen(0);   //使用的行
                    Val d2 = ((Ident) result).getDimLen(1);   //使用的列
                    int lie = ((VarItem) resItem).getDimLen(1);     //得到第二维长度 即有多少列
                    int index = getConstExp(d1) * lie + getConstExp(d2);
                    ((VarItem) resItem).addValue(resValue);
                }
            } else {        //TempVar
                ((TempVar) result).setValue(resValue);
            }
        }
        if ((resItem instanceof VarItem) && (((VarItem) resItem).getDim() != 0)) {  //数组一定要写内存
            resReg.setConflict(true);   //！！！！！记得conflict!!!
            writeRegToMem(result, resItem, resReg);    //数组写内存后要释放寄存器
            resReg.setConflict(false);
            resItem.setReg(null);
            resReg.setItem(null);
            resReg.setFree(true);
        } else if (resItem.getStack().equals("$gp")) {
            resReg.setConflict(true);
            addComment("全局变量要写内存 " + resItem.getName());
            writeRegToMem(result, resItem, resReg);    //全局变量要写到内存
            resReg.setConflict(false);
            resItem.setReg(null);
            resReg.setItem(null);
            resReg.setFree(true);
        }
    }

    /**
     * 获得数组操作的内存地址
     */
    private Register getArrayMem(int d, VarItem vi, Ident use) {
        Register arrAddr = null;
        if (d == 1) {
            Val d1 = use.getDimLen(0);    //使用维度 d1
            arrAddr = getReg();    //数组不需要设置free-false
            arrAddr.setConflict(true);
            Integer i;
            if ((i = getOpValue(d1)) != null) {     //偏移为i
                if (vi.isParm()) {
                    addComment("数组是参数 取数组基地址 一维 " + vi.getName());
                    addlwsw("lw", arrAddr.getName(), vi.getOffset(), vi.getStack());  //取出参数数组基地址
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(4 * i));
                } else {
                    addComment("数组不是参数 取数组基地址 一维 " + vi.getName());
                    add3op("addu", arrAddr.getName(), vi.getStack(), String.valueOf(vi.getOffset() + 4 * i));
                }
            } else {
                Register d1reg = getOperandReg(d1);   //a[b]  addr = arraddr + b << 2
                assert d1reg != null;
                add3op("sll", arrAddr.getName(), d1reg.getName(), String.valueOf(2)); //b << 2
                if (vi.isParm()) {
                    Register paraArrBase = getReg();
                    addComment("数组是参数 取数组基地址 一维 " + vi.getName());
                    addlwsw("lw", paraArrBase.getName(), vi.getOffset(), vi.getStack());  //取出参数数组基地址
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), paraArrBase.getName());
                } else {
                    addComment("数组不是参数 取数组基地址 一维 " + vi.getName());
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(vi.getOffset()));
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), vi.getStack());
                }
            }
            arrAddr.setConflict(false);
        } else if (d == 2) {
            Val d1 = use.getDimLen(0);   //使用的行
            Val d2 = use.getDimLen(1);   //使用的列

            int lie = vi.getDimLen(1);     //得到第二维长度 即有多少列
            Integer di1 = getOpValue(d1);
            Integer di2 = getOpValue(d2);
            arrAddr = getReg();
            arrAddr.setConflict(true);
            if ((di1 != null) && (di2 != null)) {
                if (vi.isParm()) {
                    int addr = (di1 * lie + di2) * 4;
                    addComment("数组是参数 取数组基地址 二维 " + vi.getName());
                    addlwsw("lw", arrAddr.getName(), vi.getOffset(), vi.getStack());
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(addr));
                } else {
                    int addr = vi.getOffset() + (di1 * lie + di2) * 4;
                    add3op("addu", arrAddr.getName(), vi.getStack(), String.valueOf(addr));
                }
            } else if ((di1 == null) && (di2 != null)) {
                Register d1reg = getOperandReg(d1);
                assert d1reg != null;
                add3op("mul", arrAddr.getName(), d1reg.getName(), String.valueOf(lie));
                add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(di2));
                add3op("sll", arrAddr.getName(), arrAddr.getName(), String.valueOf(2));
                if (vi.isParm()) {
                    Register paraArrBase = getReg();
                    addComment("数组是参数 取数组基地址 二维 " + vi.getName());
                    addlwsw("lw", paraArrBase.getName(), vi.getOffset(), vi.getStack());  //取出参数数组基地址
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), paraArrBase.getName());
                } else {
                    addComment("数组不是参数 取数组基地址 二维 " + vi.getName());
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(vi.getOffset()));
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), vi.getStack());
                }
            } else if (di1 != null) {
                Register d2reg = getOperandReg(d2);
                assert d2reg != null;
                add3op("addu", arrAddr.getName(), d2reg.getName(), String.valueOf(di1 * lie));
                add3op("sll", arrAddr.getName(), arrAddr.getName(), String.valueOf(2));
                if (vi.isParm()) {
                    Register paraArrBase = getReg();
                    addComment("数组是参数 取数组基地址 二维 " + vi.getName());
                    addlwsw("lw", paraArrBase.getName(), vi.getOffset(), vi.getStack());  //取出参数数组基地址
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), paraArrBase.getName());
                } else {
                    addComment("数组不是参数 取数组基地址 二维 " + vi.getName());
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(vi.getOffset()));
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), vi.getStack());
                }
            } else {
                Register d1reg = getOperandReg(d1);
                assert d1reg != null;
                add3op("mul", arrAddr.getName(), d1reg.getName(), String.valueOf(lie));
                d1reg.setConflict(true);  //统一表达式的两个操作数的寄存器是冲突的
                Register d2reg = getOperandReg(d2);
                d1reg.setConflict(false);
                assert d2reg != null;
                add3op("addu", arrAddr.getName(), arrAddr.getName(), d2reg.getName());
                add3op("sll", arrAddr.getName(), arrAddr.getName(), String.valueOf(2));
                if (vi.isParm()) {
                    Register paraArrBase = getReg();
                    addlwsw("lw", paraArrBase.getName(), vi.getOffset(), vi.getStack());  //取出参数数组基地址
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), paraArrBase.getName());
                } else {
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), String.valueOf(vi.getOffset()));
                    add3op("addu", arrAddr.getName(), arrAddr.getName(), vi.getStack());
                }
            }
            arrAddr.setConflict(false);
        }
        return arrAddr;
    }

    /**
     * 把寄存器的值写回内存
     * 若 resValue 为 null 则清空符号表里的值和寄存器，写回内存
     */
    private void writeRegToMem(Val result, ValItem valItem, Register reg) {
        if (valItem instanceof VarItem) {
            VarItem vi = (VarItem) valItem;
            int d;
            if (result == null) {
                d = 0;
            } else {
                d = ((Ident) result).getDim();
            }
            if (d == 0) {
                addlwsw("sw", reg.getName(), vi.getOffset(), vi.getStack());
            } else {    //数组要free寄存器
                addComment("数组要写内存 不在寄存器里存值 " + valItem.getName());
                Register arrAddr = getArrayMem(d, vi, (Ident) result);
                addlwsw("sw", reg.getName(), 0, arrAddr.getName());
            }
        } else if (valItem instanceof TempVar) {
            addlwsw("sw", reg.getName(), valItem.getOffset(), valItem.getStack());
        }   //没有其他情况
        valItem.setReg(null);
    }

    /**
     * 数组 或 符号表里不知道值 从内存里取出并分配寄存器
     * 都是 lw 指令，返回 lw 的寄存器
     */
    private Register getOperandReg(Val val) {  //为操作数分寄存器
        if (val instanceof Ident) {
            VarItem vi = findVarItem((val.getName()));
            assert vi != null;
            int d = ((Ident) val).getDim();
            if (d == 0) {  //使用维度为0 不是数组
                if (vi.getStack().equals("$gp")) {  //bug全局变量要直接操作内存
                    Register reg = getReg();
                    addComment("全局变量要读内存 " + vi.getName());
                    addlwsw("lw", reg.getName(), vi.getOffset(), vi.getStack());
                    return reg;
                }
                if (vi.getReg() != null) {
                    addComment("getOperandReg 寄存器 " + vi.getReg().getName() + " 里存了 " + vi.getName() + " 的值");
                    return vi.getReg();
                }
                Register reg = getReg();
                vi.setReg(reg);
                reg.setFree(false);
                reg.setItem(vi);
                addComment(vi.getName() + " 的值不在寄存器里，要从内存里取出来 " + reg.getName());
                addlwsw("lw", reg.getName(), vi.getOffset(), vi.getStack());
                return reg;
            } else {
                addComment("数组要读内存 " + vi.getName());
                Register arrAddr = getArrayMem(d, vi, (Ident) val);
                addlwsw("lw", arrAddr.getName(), 0, arrAddr.getName());//取数组值 地址和目标是一个寄存器
                return arrAddr;
            }
        } else if (val instanceof TempVar) {
            if (((TempVar) val).getReg() != null) {
                addComment("寄存器 " + ((TempVar) val).getReg().getName() + " 里存了 " + val.getName() + " 的值");
                return ((TempVar) val).getReg();
            } else {
                Register tempReg = getReg();
                ((TempVar) val).setReg(tempReg);
                tempReg.setItem((TempVar) val);
                tempReg.setFree(false);
                addComment("临时变量在内存里 " + val.getName());
                addlwsw("lw", tempReg.getName(), ((TempVar) val).getOffset(), ((TempVar) val).getStack());
                return tempReg;
            }
        } else if (val instanceof RetValue) {   //$v0只用于保存返回值
            return new Register("$v0");
        } else {    //immediate 不会出现
            System.out.println("?????? imm");
            return null;
        }
    }

    /**
     * 试图从符号表里找到值
     * 只能找 立即数 和 常量非数组 和 用于计算常数的临时变量 的值
     * 找不到返回null
     */
    private Integer getOpValue(Val val) {
        return getConstExp(val);
    }

    public int calculate(int op1, String op, int op2) {
        switch (op) {
            case "+":
                return op1 + op2;
            case "-":
                return op1 - op2;
            case "*":
                return op1 * op2;
            case "/":
                return op1 / op2;
            case "%":
                return op1 % op2;
            case "<":
                if (op1 < op2) {
                    return 1;
                } else {
                    return 0;
                }
            case "<=":
                if (op1 <= op2) {
                    return 1;
                } else {
                    return 0;
                }
            case ">":
                if (op1 > op2) {
                    return 1;
                } else {
                    return 0;
                }
            case ">=":
                if (op1 >= op2) {
                    return 1;
                } else {
                    return 0;
                }
            case "!=":
                if (op1 != op2) {
                    return 1;
                } else {
                    return 0;
                }
            case "==":
                if (op1 == op2) {
                    return 1;
                } else {
                    return 0;
                }
            default: //(op.equals("!")) {
                if (!op.equals("!")) {
                    System.out.println("应该是! calculate");
                }
                if (op2 == 0) {
                    return 1;
                } else {
                    return 0;
                }
        }
    }

    private VarItem findVarItem(String n) {
        int l = tables.size();
        VarItem v;
        for (int i = l - 1; i >= 0; --i) {
            if ((v = tables.get(i).findIdent(n)) != null) {
                return v;
            }
        }
        System.out.println("var null error =====");
        return null;
    }

    private TempVar findTemp(String n) {
        int l = tables.size();
        TempVar t;
        for (int i = l - 1; i >= 0; --i) {
            if ((t = tables.get(i).findTemp(n)) != null) {
                return t;
            }
        }
        //System.out.println("temp null error =====");
        return null;
    }

    public void returnCode(Return r) {
        Val vr = r.getRetExp();
        if (vr != null) {
            Integer i;
            if ((i = getOpValue(vr)) != null) {
                addLi("$v0", i);
            } else {
                Register reg = getOperandReg(vr);
                assert reg != null;
                addMove("$v0", reg.getName());   //可加优化，给setOperandReg 加参数(结果寄存器) 省去move操作
            }
        }
        mips.add("jr $ra");
        mips.add("\n");
    }

    public void cmpCode(CmpBranch branch) {
        Val o1 = branch.getOperand1();
        Val o2 = branch.getOperand2();
        Integer i;
        Register reg1;
        Register reg2;
        if ((i = getOpValue(o1)) != null) {
            if (i == 0) {
                reg1 = new Register("$zero");
            } else {
                reg1 = getReg();
                addLi(reg1.getName(), i);
            }
        } else {
            reg1 = getOperandReg(o1);
        }
        assert reg1 != null;
        reg1.setConflict(true);
        if ((i = getOpValue(o2)) != null) {
            if (i == 0) {
                reg2 = new Register("$zero");
            } else {
                reg2 = getReg();
                addLi(reg2.getName(), i);
            }
        } else {
            reg2 = getOperandReg(o2);
        }
        reg1.setConflict(false);
        assert reg2 != null;
        add3op(branch.getCmpOp(), reg1.getName(), reg2.getName(), branch.getLabel());
    }

    private void getInt(GetInt g) {
        addSyscall(5);
        Val lval = g.getLval();
        VarItem vi = findVarItem(lval.getName());
        assert vi != null;
        assert lval instanceof Ident;
        if (vi.getReg() != null) {      //!!!!bugggg输入一个数之后，要把这个数原来占的寄存器释放
            vi.getReg().setItem(null);
            vi.getReg().setFree(true);
        }
        writeRegToMem(g.getLval(), vi, new Register("$v0"));
    }

    private void printf(Printf p) {
        if (p.getS() != null) {
            mips.add("la $a0, " + p.getS());
            addSyscall(4);
        } else {    //p.getVal != null
            Val v = p.getVal();
            assert v != null;
            Integer i;
            if ((i = getOpValue(v)) != null) {
                //System.out.println(v.getName() + " " + i);
                addLi("$a0", i);
            } else {
                Register reg = getOperandReg(v);
                assert reg != null;
                if (reg.getItem() != null) {
                    addComment("#输出 " + reg.getItem().getName());
                }
                addMove("$a0", reg.getName());
            }
            addSyscall(1);
        }
    }

    private void blockSe(BlockSe b) {
        if (b.getSe().equals("end")) {
            SymTable tt = tables.pop();
            nowTable = tables.peek();
            if (b.isFunc()) {
                fparaNum = -1;
                //bug  每个函数块结束后也要释放所有寄存器！！！
                for (Register r : RegMana.getSavedReg()) {
                    if (r.getItem() != null) {
                        r.getItem().setReg(null);
                        r.setItem(null);
                    }
                    r.setFree(true);
                }
            }
            for (TempVar t : tt.getTempTable()) { //每个块结束之后要释放寄存器
                if (t.getReg() != null) {
                    t.getReg().setFree(true);
                    t.getReg().setItem(null);
                    t.setReg(null);
                }
            }
            for (VarItem v : tt.getVarTable()) {
                if (v.getReg() != null) {
                    v.getReg().setFree(true);
                    v.getReg().setItem(null);
                    v.setReg(null);
                }
            }
        } else if (b.getSe().equals("start") && !b.isFunc()) {
            nowTable = new SymTable();
            tables.push(nowTable);
        }
    }

    private void funcDef(FunctionDef f) {
        nowTable = new SymTable();
        tables.push(nowTable);
        mips.add("func_" + f.getName() + ":");
        this.nowOffset = 0;     // !!!!!
        this.tempOffset = 0;
    }

    private void funcFpara(FuncFpara f) {       //相当于定义
        fparaNum++;
        VarItem vi = new VarItem(f.getName(), false, f.getDim(), "$fp");
        vi.setValue(null);
        vi.setParm();
        if (f.getDim() == 1) {
            vi.setDimLen(null);
        } else if (f.getDim() == 2) {
            vi.setDimLen(null);
            vi.setDimLen(getConstExp(f.getLen_2dim()));
        }
        setOffset(vi, 4);
        nowTable.addVarItem(vi);
    }

    private void pushRpara(PushRpara p) {
        rparaNum++;  //第几个参数  0 1 2 3
        int saveRegSpace = RegMana.getSavedNum() * 4;   //!!!先给保存寄存器留出空间再push参数
        Val v = p.getParam();
        if (v instanceof Ident) {
            VarItem item = findVarItem(v.getName());
            Ident use = (Ident) v;
            assert item != null;
            int dim = item.getDim() - use.getDim();
            if (dim == 0) {
                Integer i = null;
                if ((i = getOpValue(use)) != null) {
                    Register reg = getReg();
                    mips.add("li " + reg.getName() + ", " + i);
                    addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
                } else {
                    Register reg = getOperandReg(use);
                    assert reg != null;
                    addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
                }
            } else if (use.getDim() == 0) {      // dim = 1 or 2 使用维度为0,直接取基地址
                Register reg = getReg();
                if (item.isParm()) {
                    addlwsw("lw", reg.getName(), item.getOffset(), item.getStack());  //取出参数数组基地址
                } else {
                    add3op("addu", reg.getName(), item.getStack(), String.valueOf(item.getOffset()));
                }
                addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
            } else {   // dim = 1 or 2 使用维度为1，定义维度为2
                Register reg = getReg();
                if (item.isParm()) {
                    addlwsw("lw", reg.getName(), item.getOffset(), item.getStack());  //取出参数数组基地址
                } else {
                    add3op("addu", reg.getName(), item.getStack(), String.valueOf(item.getOffset()));
                }       //此时reg为二维数组的基地址
                Val used1 = use.getDimLen(0);
                int lie = item.getDimLen(1);
                Integer i = null;
                if ((i = getOpValue(used1)) != null) {
                    add3op("addu", reg.getName(), reg.getName(), String.valueOf(lie * i * 4));
                } else {
                    reg.setConflict(true);
                    Register dim1reg = getOperandReg(used1);
                    assert dim1reg != null;
                    dim1reg.setConflict(true);
                    Register usedOffset = getReg();
                    dim1reg.setConflict(false);
                    reg.setConflict(false);
                    add3op("mul", usedOffset.getName(), dim1reg.getName(), String.valueOf(lie));     //行×列
                    add3op("sll", usedOffset.getName(), usedOffset.getName(), String.valueOf(2)); //×4
                    add3op("addu", reg.getName(), reg.getName(), usedOffset.getName());
                }
                addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
            }
        } else if (v instanceof TempVar) {
            Integer i = null;
            if ((i = getOpValue(v)) != null) {
                Register reg = getReg();
                mips.add("li " + reg.getName() + ", " + i);
                addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
            } else {
                Register reg = getOperandReg(v);
                assert reg != null;
                addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
            }
        } else if (v instanceof Immediate) {
            Register reg = getReg();
            mips.add("li " + reg.getName() + ", " + ((Immediate) v).getValue());
            addlwsw("sw", reg.getName(), saveRegSpace + nowOffset + rparaNum * 4, "$fp");
        } else {    //RET
            addlwsw("sw", "$v0", saveRegSpace + nowOffset + rparaNum * 4, "$fp");
        }
    }

    private void saveReg() {
        saved = true;
        for (Register r : RegMana.getSavedReg()) {
            addlwsw("sw", r.getName(), nowOffset, "$fp");
            nowOffset += 4;
        }
    }

    private void restoreReg() {
        int l = RegMana.getSavedNum();
        for (int i = l - 1; i >= 0; --i) {
            nowOffset -= 4; //!!!!!!!!!应该放在lw前
            addlwsw("lw", RegMana.getSavedReg().get(i).getName(), nowOffset, "$fp");
        }
    }

    private void funcCall(FunctionCall f) {
        rparaNum = -1;
        //if (!saved) {   //说明没有参数
        saveReg();
        //}
        add3op("addu", "$fp", "$fp", String.valueOf(nowOffset));
        add3op("addu", "$sp", "$sp", String.valueOf(tempOffset));
        mips.add("jal " + "func_" + f.getName());
        mips.add("nop\n");
        saved = false;
        add3op("subu", "$fp", "$fp", String.valueOf(nowOffset));
        add3op("subu", "$sp", "$sp", String.valueOf(tempOffset));
        restoreReg();
    }

    //在mips中添加mips代码：

    private void addLi(String reg, int i) {
        mips.add("li " + reg + ", " + i);
    }

    private void addSyscall(int i) {
        mips.add("li $v0, " + i);
        mips.add("syscall");
    }

    private void addMove(String reg1, String reg2) {
        mips.add("move " + reg1 + ", " + reg2);
    }

    private void add3op(String instruction, String reg1, String reg2, String reg3) {
        mips.add(instruction + " " + reg1 + ", " + reg2 + ", " + reg3);
    }

    private void addlwsw(String instruction, String reg1, int offset, String reg2) {
        mips.add(instruction + " " + reg1 + ", " + offset + "(" + reg2 + ")");
    }

    private void addComment(String s) {
        mips.add("# " + s);
    }

    public void writeMips() throws IOException {
        FileWriter fileWriter = new FileWriter("mips.txt");
        for (String s : mips) {
            fileWriter.write(s);
            fileWriter.write("\n");
        }
        fileWriter.close();
    }
}
