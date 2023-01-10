package wordanalysis.words;

public abstract class Word {
    private final int line;
    private final String str;

    public Word(int line, String str) {
        this.line = line;
        this.str = str;
    }

    public int getLine() {
        return line;
    }

    public String getStr() {
        return str;
    }

    public abstract String getTypeCode();
}
