package syntaxanalysis.parser;

import mid.tree.BlockNode;
import mid.tree.CompUnitNode;
import mid.tree.DeclNode;
import mid.tree.FuncDefNode;
import syntaxanalysis.symtable.SymbolTable;

public class CompUnitParser extends Parser {
    private final String unit = "CompUnit";

    public CompUnitParser() {
        super();
        this.treeNode = new CompUnitNode();
        analyze();
        output.addSyntax("<" + unit + ">");
    }

    @Override
    public void analyze() {
        //当前单词
        String word;
        //预读两个单词 再回退
        String word2;
        //CompUnit → {Decl} {FuncDef} MainFuncDef
        while (true) {  //Decl
            word = nowWord.getStr();
            lexer.getWord();
            word2 = lexer.getWord().getStr();
            lexer.goBack(2);
            if (word.equals("const") || (word.equals("int") && !word2.equals("("))) { //Decl
                if (TABLES.empty()) {
                    TABLES.push(new SymbolTable());
                }
                DeclParser declParser = new DeclParser();
                ((CompUnitNode) treeNode).addDecl((DeclNode) declParser.treeNode);
            } else {
                break;
            }
        }

        //预读两个单词
        String word1;
        while (true) {
            word = nowWord.getStr();
            word1 = lexer.getWord().getStr();
            word2 = lexer.getWord().getStr();
            lexer.goBack(2);
            if (word.equals("void") || (word.equals("int") && !word1.equals("main") && word2.equals("("))) {
                if (TABLES.empty()) {
                    TABLES.push(new SymbolTable());
                }
                FuncDefParser funcDefParser = new FuncDefParser(word);
                ((CompUnitNode) treeNode).addFuncDef((FuncDefNode) funcDefParser.treeNode);
            } else {
                break;
            }
        }

        if (TABLES.empty()) {
            TABLES.push(new SymbolTable());
        }
        MainFuncDefParser main = new MainFuncDefParser();
        ((CompUnitNode) treeNode).setMainBlock((BlockNode) main.treeNode);

        //多个 int main()
        while (!lexer.wordEnd()) {
            new MainFuncDefParser();
        }
        compileOver = true;
    }
}
