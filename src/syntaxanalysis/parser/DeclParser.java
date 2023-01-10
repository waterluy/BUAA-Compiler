package syntaxanalysis.parser;

public class DeclParser extends Parser {
    private final String unit = "Decl";

    public DeclParser() {
        super();
        analyze();
        // no <Decl>
    }

    @Override
    public void analyze() {
        //Decl → ConstDecl | VarDecl
        if (nowWord.getStr().equals("const")) {
            ConstDeclParser constDeclParser = new ConstDeclParser();
            this.treeNode = constDeclParser.treeNode;
        } else if (nowWord.getStr().equals("int")) {
            VarDeclParser varDeclParser = new VarDeclParser();
            this.treeNode = varDeclParser.treeNode;
        } else {
            error();
        }
    }
}
