package syntaxanalysis.parser;

import mid.tree.CompUnitNode;
import mid.tree.TreeNode;
import syntaxanalysis.symtable.FunctionItem;
import syntaxanalysis.symtable.SymbolTable;
import syntaxanalysis.symtable.VariableItem;
import tools.Output;
import wordanalysis.Lexer;
import wordanalysis.words.Word;

import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    protected static Lexer lexer;

    static {
        try {
            lexer = new Lexer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //syntax analysis
    protected static Word nowWord;
    private static final boolean output2file = false;
    protected static final Output output = new Output();
    protected static boolean compileOver = false;

    //symTable error
    protected static final Stack<SymbolTable> TABLES = new Stack<>();
    private boolean debugError = false;
    private static final boolean output2error = true;
    protected Integer dim = -1;  //不是static

    //mid build tree node
    protected TreeNode treeNode = null;
    private CompUnitNode compUnitNode;

    public Parser() {

    }

    public CompUnitNode getCompUnitNode() {
        return compUnitNode;
    }

    public int getDim() {
        return dim;
    }

    public void analyze() throws IOException {
        readWord();
        CompUnitParser compUnitParser = new CompUnitParser();
        this.compUnitNode = (CompUnitNode) compUnitParser.treeNode;
        if (output2file) {
            output.writeSyntax();
        }
        if (output2error) {
            output.writeError();
        }
    }

    protected void readWord() {
        nowWord = lexer.getWord();
        if (nowWord != null) {
            output.addWord(nowWord.getTypeCode() + " " + nowWord.getStr());
        } else {
            output.addWord(null);
        }
    }

    protected void error() {
        System.out.println("error aaaaaaaaaaaaaaaaaaaaaaaaaaa~   " + nowWord.getLine() + " " + nowWord.getStr());
    }

    protected int checkFormatStr() { //error  nowWord
        String word = nowWord.getStr();
        String pattern = "[^ !(-\\[\\]-~]";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word.replace("%d", "").replace("\\n", "")
                                    .replace("\"", ""));
        if (m.find()) {
            handleError(nowWord.getLine(), "a", word);
        }
        int index = 0;
        int count = 0;
        while ((index = word.indexOf("%d", index)) != -1) {
            count++;
            index += 2;
        }
        return count;
    }

    protected boolean checkReDef(SymbolTable nowTable, Word nowWord) {
        String name = nowWord.getStr();
        boolean reDef = nowTable.checkReDef(name);
        if (reDef) {
            handleError(nowWord.getLine(), "b", name);
        }
        return reDef;
    }

    protected VariableItem checkNotDefVar(Word nowWord) {
        String name = nowWord.getStr();
        int l = TABLES.size();
        VariableItem v;
        for (int i = l - 1; i >= 0; --i) {
            if ((v = TABLES.get(i).checkNotDefVar(name)) != null) {
                return v;
            }
        }
        handleError(nowWord.getLine(), "c", name);
        return null;
    }

    protected FunctionItem checkNotDefFunc(Word nowWord) {
        String name = nowWord.getStr();
        int l = TABLES.size();
        FunctionItem f;
        for (int i = l - 1; i >= 0; --i) {
            if ((f = TABLES.get(i).checkNotDefFunc(name)) != null) {
                return f;
            }
        }
        handleError(nowWord.getLine(), "c", name);
        return null;
    }

    protected void checkConstAssign(Word word) {
        String name = word.getStr();
        int l = TABLES.size();
        for (int i = l - 1; i >= 0; --i) {
            if (TABLES.get(i).checkNotDefVar(name) != null) {   //该层符号表定义了var
                if (TABLES.get(i).checkConstAssign(name)) { //是常量
                    handleError(nowWord.getLine(), "h", name);
                }
                return;
            }
        }
    }

    protected void handleError(int line, String errorType, String name) {
        if (!debugError) {
            output.addError(line + " " + errorType);
        } else {
            output.addError(line + " " + errorType + " " + name);
        }
    }

    public int getErrorNum() {
        return output.getErrorNum();
    }
}
