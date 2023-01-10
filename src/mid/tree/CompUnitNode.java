package mid.tree;

import mid.AllMidCodes;
import mid.midcodes.FunctionDef;

import java.util.ArrayList;

public class CompUnitNode implements TreeNode {
    private final ArrayList<DeclNode> decls;
    private final ArrayList<FuncDefNode> funcDefs;
    private BlockNode mainBlock;

    public CompUnitNode() {
        this.decls = new ArrayList<>();
        this.funcDefs = new ArrayList<>();
    }

    public void addDecl(DeclNode decl) {
        this.decls.add(decl);
    }

    public void addFuncDef(FuncDefNode funcDef) {
        this.funcDefs.add(funcDef);
    }

    public void setMainBlock(BlockNode m) {
        this.mainBlock = m;
        this.mainBlock.setFunc();
    }

    @Override
    public void generateMidCode() {
        for (DeclNode d : decls) {
            d.generateMidCode();
        }
        for (FuncDefNode f : funcDefs) {
            f.generateMidCode();
        }
        FunctionDef mainDef = new FunctionDef("int", "main", false);
        AllMidCodes.addMidCode(mainDef);
        mainBlock.generateMidCode();
    }
}
