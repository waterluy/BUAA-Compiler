package wordanalysis;

import wordanalysis.words.Delimiter;
import wordanalysis.words.FormatStr;
import wordanalysis.words.Ident;
import wordanalysis.words.IntConst;
import wordanalysis.words.KeyWord;
import wordanalysis.words.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Matcher;

public class Lexer {
    private final String code; // 源代码每行以'\n'分隔
    private int strIndex;
    private final int codeLength;
    private int nowLine;
    private final ArrayList<Word> wordList; //all words in code
    private int wordIndex;
    private static final String TESTFILE = "testfile.txt";

    /*
     * 构造时解析出所有单词存到单词表里
     * getword 每次取出一个单词
     */
    public Lexer() throws IOException {
        this.code = readCode();
        //System.out.println(code);
        this.strIndex = 0;
        this.codeLength = code.length();
        this.nowLine = 1;
        this.wordList = new ArrayList<>();
        this.wordIndex = 0;

        while (strIndex < codeLength) {
            //跳过所有空白和注释 再读一个单词
            passInvalid();
            String matchStr = "";
            // 关键字
            Matcher matcher = KeyWord.PATTERN.matcher(code.substring(strIndex));
            if (matcher.find()) {
                matchStr = matcher.group(1);
                strIndex += matchStr.length();
                KeyWord word = new KeyWord(nowLine, matchStr);
                wordList.add(word);
                //System.out.println(matchStr);
                continue;
            }
            //标识符
            matcher = Ident.PATTERN.matcher(code.substring(strIndex));
            if (matcher.find()) {
                matchStr = matcher.group(1);
                strIndex += matchStr.length();
                Ident word = new Ident(nowLine, matchStr);
                wordList.add(word);
                //System.out.println(matchStr);
                continue;
            }
            //常数
            matcher = IntConst.PATTERN.matcher(code.substring(strIndex));
            if (matcher.find()) {
                matchStr = matcher.group(1);
                strIndex += matchStr.length();
                IntConst word = new IntConst(nowLine, matchStr);
                wordList.add(word);
                //System.out.println(matchStr);
                continue;
            }
            //格式化字符串
            matcher = FormatStr.PATTERN.matcher(code.substring(strIndex));
            if (matcher.find()) {
                matchStr = matcher.group(1);
                strIndex += matchStr.length();
                FormatStr word = new FormatStr(nowLine, matchStr);
                wordList.add(word);
                //System.out.println(matchStr);
                continue;
            }
            //分界符
            matcher = Delimiter.PATTERN.matcher(code.substring(strIndex));
            if (matcher.find()) {
                matchStr = matcher.group(1);
                strIndex += matchStr.length();
                Delimiter word = new Delimiter(nowLine, matchStr);
                wordList.add(word);
                //System.out.println(matchStr);
            }
        }
    }

    private void passInvalid() {
        while (true) {
            boolean hasBlank = passBlank();  // 可以一次性跳过所有空白
            boolean hasComment = passComment();  // 可以一次性跳过所有注释
            if ((!hasBlank) && (!hasComment)) {  // 没有空白也没有注释了
                break;
            }
        }
    }

    private boolean passBlank() {  // 可以一次性跳过所有空白
        boolean flag = false;       // 标记本轮是否有空白
        while (strIndex < codeLength) {
            char c = code.charAt(strIndex);
            if ((c == ' ') || (c == '\t')) {
                flag = true;
                strIndex++;
            } else if (c == '\n') {
                strIndex++;
                nowLine++;
            } else {
                break;
            }
        }
        return flag;
    }

    private boolean passComment() {  // 可以一次性跳过所有注释
        boolean flag = false;
        while (strIndex < codeLength) {
            if ((strIndex < codeLength - 1) &&
                    (code.charAt(strIndex) == '/') && (code.charAt(strIndex + 1) == '/')) {
                flag = true;
                strIndex += 2;
                while (strIndex < codeLength) {
                    if (code.charAt(strIndex) == '\n') {
                        strIndex++;
                        nowLine++;
                        break;
                    }
                    strIndex++;
                }
            } else if ((strIndex < codeLength - 1) &&
                    (code.charAt(strIndex) == '/') && (code.charAt(strIndex + 1) == '*')) {
                //int num = 1; // /* 的个数
                flag = true;
                strIndex += 2;
                while (strIndex < codeLength) {
                    if ((strIndex < codeLength - 1) &&
                            (code.charAt(strIndex) == '*') && (code.charAt(strIndex + 1) == '/')) {
                        strIndex += 2;
                        break;
                    } else if (code.charAt(strIndex) == '\n') {
                        strIndex++;
                        nowLine++;
                    } else {
                        strIndex++;
                    }
                }
            } else {
                break;
            }
        }
        return flag;
    }

    public Word getWord() {
        if (wordEnd()) {
            return null;
        }
        wordIndex++;
        return wordList.get(wordIndex - 1);
    }

    public int getLastWordLine() { //缺少分号时，上一个非终结符的行号
        return wordList.get(wordIndex - 2).getLine();
    }

    public int getWordIndex() {
        return wordIndex;
    }

    public boolean wordEnd() { //是否读完所有单词
        return (wordIndex == wordList.size());
    }

    public void goBack(int n) {
        wordIndex -= n;
    }

    public static String readCode() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(TESTFILE));
        StringJoiner sj = new StringJoiner("\n");
        String line;
        while ((line = reader.readLine()) != null) {
            sj.add(line);
        }
        reader.close();
        //System.out.println(sj);
        return sj.toString();
    }
}