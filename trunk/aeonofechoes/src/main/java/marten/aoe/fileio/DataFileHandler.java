package marten.aoe.fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public final class DataFileHandler {
    private DataFileHandler() {}
    private static final int MAX_LINE_LENGTH = 1024;
    private static final String LIST_START = "\\s*\\S+\\s*:\\s*";
    private static final String KEY_VALUE_PAIR = "\\s*\\S+?\\s*=\\s*\\S+\\s*";
    private static final String SINGLETON = "\\s*\\S+\\s*";
    private static final String EMPTY_LINE = "\\s*";
    private static final String COMMENT = "\\s*#.*";
    
    public static void write(String fileName, DataTree data) throws IOException {
    	BufferedWriter file = new BufferedWriter(new FileWriter(fileName));
    	write(file, 0, data);
    }
    
    private static void write(BufferedWriter file, int level, DataTree data) throws IOException {
        String identation = "";
        for (int index = 0; index < level; index++) {
            identation += "    ";
        }
        for (DataTree branch : data.branches()) {
            if (branch.value().equals("KEYVALUE")) {
                file.write(identation + branch.branches().get(0).value().replace(' ', '_') + " = " + branch.branches().get(1).value().replace(' ', '_') + "\n");
            } else if (branch.branches().isEmpty()) {
                file.write(identation + branch.value().replace(' ', '_') + "\n");                
            } else {
                file.write(identation + branch.value().replace(' ', '_') + ":\n");
                write(file, level + 1, branch);
            }            
        }
        file.close();
    }
    
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
            if (line.matches(LIST_START)) {
                answer.addBranch(read(file, level + 1, line.split(":")[0].trim().replace('_', ' ')));
            }
            else if (line.matches(KEY_VALUE_PAIR)) {
                String[] pair = line.split("=");
                String key = pair[0].trim().replace('_', ' ');
                String value = "";
                for (int index = 1; index < pair.length; index++)
                    value += pair[index];
                DataTree branch = new DataTree("KEYVALUE");
                branch.addBranch(new DataTree(key));
                branch.addBranch(new DataTree(value.trim().replace('_', ' ')));
                answer.addBranch(branch);                
            } else if (line.matches(SINGLETON)) {
                answer.addBranch(new DataTree(line.trim().replace('_', ' ')));
            }
            file.mark(MAX_LINE_LENGTH);
            line = file.readLine();                       
        }
        file.reset();        
        return answer;
    }
}
