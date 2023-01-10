package mid;

import mid.midcodes.ConstStr;
import mid.midcodes.MidCode;
import mid.tree.CompUnitNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AllMidCodes {
    private final CompUnitNode compUnitNode;
    private static final ArrayList<MidCode> MIDCODES = new ArrayList<>();
    private static final ArrayList<ConstStr> CONST_STRS = new ArrayList<>();

    public AllMidCodes(CompUnitNode cu) {
        this.compUnitNode = cu;
    }

    public void generate() {
        compUnitNode.generateMidCode();
    }

    public static void addMidCode(MidCode mc) {
        MIDCODES.add(mc);
        if (mc instanceof ConstStr) {
            CONST_STRS.add((ConstStr) mc);
        }
    }

    public void writeMidCodes() throws IOException {
        FileWriter writer = new FileWriter("mid.txt");
        for (MidCode m : MIDCODES) {
            writer.write(m.toString());
            writer.write("\n");
        }
        writer.close(); //!!!!!
    }

    public static ArrayList<MidCode> getMIDCODES() {
        return MIDCODES;
    }

    public static ArrayList<ConstStr> getConstStrs() {
        return CONST_STRS;
    }
}
