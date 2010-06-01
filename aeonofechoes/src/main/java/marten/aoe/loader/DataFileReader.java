package marten.aoe.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class DataFileReader {
    private DataFileReader() {}
    private static final int MAX_LINE_LENGTH = 1024;
    private static final String LIST_START = "\\s*\\w+\\s*:\\s*";
    private static final String KEY_VALUE_PAIR = "\\s*\\w+\\s*=.*";
    private static final String SINGLETON = "\\s*\\w+\\s*";
    private static final String EMPTY_LINE = "\\s*";
    private static final String COMMENT = "\\s*#.*";
    
    public static DataTree read(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        return read(file, 0, "FILE");
    }
    private static DataTree read(BufferedReader file, int level, String name) {
        /* DEBUG */
        System.out.println("Entering level "+level);
        /* END DEBUG */
        DataTree answer = new DataTree(name);
        String identRegExp = " {"+level*4+"}.*";
        String line = "";
        try {
            file.mark(MAX_LINE_LENGTH);
        } catch (IOException e) {
            throw new RuntimeException("Irrecoverable error at Data file reader - marking file position failed");
        }
        try {
            line = file.readLine();
        } catch (IOException e) {
            return answer;
        }
        while (line != null && (line.matches(identRegExp) || line.matches(EMPTY_LINE) || line.matches(COMMENT))) {
            if (line.matches(LIST_START)) {
                /* DEBUG */
                System.out.println("List found");
                /* END DEBUG */
                answer.addBranch(read(file, level + 1, line.split(":")[0].trim()));                
            }
            else if (line.matches(KEY_VALUE_PAIR)) {
                /* DEBUG */
                System.out.println("Key-value pair found");
                /* END DEBUG */
                String[] pair = line.split("=");
                String key = pair[0].trim();
                String value = "";
                for (int index = 1; index < pair.length; index++)
                    value += pair[index];
                DataTree branch = new DataTree("KEYVALUE");
                branch.addBranch(new DataTree(key));
                branch.addBranch(new DataTree(value));
                answer.addBranch(branch);                
            } else if (line.matches(SINGLETON)) {
                /* DEBUG */
                System.out.println("Singleton found");
                /* END DEBUG */
                answer.addBranch(new DataTree(line.trim()));                
            }
            try {
                file.mark(MAX_LINE_LENGTH);
            } catch (IOException e) {
                throw new RuntimeException("Irrecoverable error at Data file reader - marking file position failed");
            }            
            try {
                line = file.readLine();
            } catch (IOException e) {
                return answer;
            }            
        }
        try {
            file.reset();
        } catch (IOException e) {
            throw new RuntimeException("Irrecoverable error at Data file reader - returning to a marked file position failed");
        }
        return answer;
    }
}
