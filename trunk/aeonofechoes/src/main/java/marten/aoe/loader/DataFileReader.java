package marten.aoe.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

final class DataFileReader {
    private DataFileReader() {}
    private static final int MAX_LINE_LENGTH = 1024;
    private static final String LIST_START = "\\s*\\S+\\s*:\\s*";
    private static final String KEY_VALUE_PAIR = "\\s*\\S+?\\s*=\\s*\\S+\\s*";
    private static final String SINGLETON = "\\s*\\S+\\s*";
    private static final String EMPTY_LINE = "\\s*";
    private static final String COMMENT = "\\s*#.*";
    
    public static DataTree read(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        return read(file, 0, "FILE");
    }
    private static DataTree read(BufferedReader file, int level, String name) throws IOException {
        DataTree answer = new DataTree(name);
        String identRegExp = " {"+level*4+"}.*";
        String line = "";
        file.mark(MAX_LINE_LENGTH);
        line = file.readLine();
        while (line != null && (line.matches(identRegExp) || line.matches(EMPTY_LINE) || line.matches(COMMENT))) {
            if (line.matches(LIST_START))
                answer.addBranch(read(file, level + 1, line.split(":")[0].trim()));                
            else if (line.matches(KEY_VALUE_PAIR)) {
                String[] pair = line.split("=");
                String key = pair[0].trim();
                String value = "";
                for (int index = 1; index < pair.length; index++)
                    value += pair[index];
                DataTree branch = new DataTree("KEYVALUE");
                branch.addBranch(new DataTree(key));
                branch.addBranch(new DataTree(value.trim()));
                answer.addBranch(branch);                
            } else if (line.matches(SINGLETON))
                answer.addBranch(new DataTree(line.trim()));                
            file.mark(MAX_LINE_LENGTH);
            line = file.readLine();                       
        }
        file.reset();        
        return answer;
    }
}
