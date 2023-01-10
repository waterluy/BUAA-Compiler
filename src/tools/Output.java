package tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Output {
    private final String TESTFILE = "testfile.txt";
    private final String OUTPUTFILE = "output.txt";
    private final String ERRORFILE = "error.txt";
    private final ArrayList<String> lines = new ArrayList<>();
    private final ArrayList<String> errors = new ArrayList<>();

    public void addWord(String str) {
        lines.add(str);
        //System.out.println(str);
    }

    public void addSyntax(String str) {
        int n = lines.size();
        lines.add(lines.get(n - 1));
        lines.set(n - 1, str);
        //System.out.println(str);
    }

    public void goBackWord(String str) {
        int index = lines.lastIndexOf(str);
        int l = lines.size();
        for (int i = 0; i < l - index; ++i) {
            lines.remove(index);
        }
    }

    public void goBackError(String str) {
        //System.out.println("error back");
        int index = errors.lastIndexOf(str);
        int l = errors.size();
        for (int i = 0; i < l - index; ++i) {
            errors.remove(index);
        }
        //errors.remove(errors.lastIndexOf(str));
    }

    public void writeSyntax() throws IOException {
        FileWriter writer = new FileWriter(OUTPUTFILE);
        for (String line : lines) {
            if (line != null) {
                writer.write(line);
                writer.write("\n");
            }
        }
        writer.close(); //!!!!!
    }

    public void addError(String str) {
        errors.add(str);
    }

    public void writeError() throws IOException {
        FileWriter writer = new FileWriter(ERRORFILE);
        for (String e : errors) {
            writer.write(e);
            writer.write("\n");
        }
        writer.close(); //!!!!!
    }

    public int getErrorNum() {
        return errors.size();
    }
}
