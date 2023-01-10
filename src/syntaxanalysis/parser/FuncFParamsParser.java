package syntaxanalysis.parser;

import mid.tree.FparamNode;
import syntaxanalysis.symtable.SymbolTable;

import java.util.ArrayList;

public class FuncFParamsParser extends Parser {
    private final String unit = "FuncFParams";
    private final SymbolTable symTable;
    private final ArrayList<FparamNode> fparamNodes;

    public FuncFParamsParser() {
        super();
        this.symTable = TABLES.peek();
        this.fparamNodes = new ArrayList<>();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        FuncFParamParser funcFParamParser = new FuncFParamParser();
        fparamNodes.add((FparamNode) funcFParamParser.treeNode);
        while (nowWord.getStr().equals(",")) {
            readWord();
            funcFParamParser = new FuncFParamParser();
            fparamNodes.add((FparamNode) funcFParamParser.treeNode);
        }
    }

    public ArrayList<FparamNode> getFparamNodes() {
        return fparamNodes;
    }
}
