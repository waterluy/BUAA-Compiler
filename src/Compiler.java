//import back.MidToMips;
import back.MidToMips;
import mid.AllMidCodes;
import syntaxanalysis.parser.Parser;
import wordanalysis.Lexer;
import wordanalysis.words.Word;

import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public class Compiler {
    private static final String TESTFILE = "testfile.txt";
    private static final String OUTPUTFILE = "output.txt";

    private static void waTest(Lexer lexer) throws IOException {
        FileWriter writer = new FileWriter(OUTPUTFILE);
        StringJoiner sj = new StringJoiner("\n");
        Word word = lexer.getWord();
        sj.add(word.getTypeCode() + " " + word.getStr());
        while (!lexer.wordEnd()) {
            word = lexer.getWord();
            sj.add(word.getTypeCode() + " " + word.getStr());
        }
        writer.write(sj.toString());
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        //Lexer lexer = new Lexer();
        //waTest(lexer);
        //语法分析
        Parser parser = new Parser();
        parser.analyze();
        if (parser.getErrorNum() == 0) {
            //生成中间代码
            AllMidCodes allMidCodes = new AllMidCodes(parser.getCompUnitNode());
            allMidCodes.generate();
            allMidCodes.writeMidCodes();
            //生成目标代码
            MidToMips midToMips = new MidToMips(AllMidCodes.getMIDCODES(), AllMidCodes.getConstStrs());
            midToMips.toMips();
            midToMips.writeMips();
        }
    }
}
