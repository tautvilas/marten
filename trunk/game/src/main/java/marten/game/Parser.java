package marten.game;

import java.util.HashMap;

import marten.game.map.MapCoordinates;

/** The main class for handling scripts and terminal commands
 * @author carnifex*/
public class Parser {
    /**The definitions of integer variables in the script*/
    private HashMap<String,Integer> definedIntegers = new HashMap<String,Integer>();
    /**The definitions of string variables in the script*/
    private HashMap<String,String> definedStrings = new HashMap<String,String>();
    /**The definitions of tile variables in the script*/
    private HashMap<String,MapCoordinates> definedTiles = new HashMap<String,MapCoordinates>();
    
    /**A regexp that matches any integer in the script*/
    private static final String INTEGER = "\\s+\\d+";
    /**A regexp that matches any string in the script*/
    private static final String STRING = "\\s+\".*?\"";
    /**A regexp that matches any tile in the script*/
    private static final String TILE = "\\s+\\[\\d+\\s*,\\s*\\d+\\]";
    
    /**A regexp that matches any variable in the script*/
    private static final String VARIABLE = "\\s+[a-z]+";
    
    /**A regexp that matches the end of a line*/
    private static final String END = "\\s*$";
    /**A regexp that matches anything*/
    private static final String ANYTHING = ".*$";
    /**A regexp that matches quote marks*/
    private static final String QUOTE = "\"";
    /**A regexp that matches square brackets*/
    private static final String SQUARE_BRACKETS = "[\\[\\]]";
    /**A regexp that matches comma*/
    private static final String COMMA = ",";
    
    /**A regexp that matches DEFINE command*/    
    private static final String DEFINE = "^\\s*DEFINE";
    /**A regexp that matches DROP command*/
    private static final String DROP = "^\\s*DROP";
    /**A regexp that matches // (comment) command*/
    private static final String COMMENT = "^\\s*//";
    
    /**A regexp that matches AS modifier*/
    private static final String AS = "\\s+AS";
    
    private void dropVariable (String variableName) {
        this.definedIntegers.remove(variableName);
        this.definedStrings.remove(variableName);
        this.definedTiles.remove(variableName);
    }
    private boolean isDefinedVariable (String variableName) {
        return this.isDefinedInteger(variableName) || this.isDefinedString(variableName) || this.isDefinedTile(variableName);
    }
    private boolean isDefinedInteger (String variableName) {
        return this.definedIntegers.containsKey(variableName);
    }
    private boolean isDefinedString (String variableName) {
        return this.definedStrings.containsKey(variableName);
    }
    private boolean isDefinedTile (String variableName) {
        return this.definedTiles.containsKey(variableName);
    }
    
    public Return parseLine (String scriptLine) {
        if (scriptLine.matches(DEFINE+VARIABLE+AS+INTEGER+END))
            return this.parseDefineInteger(scriptLine);
        if (scriptLine.matches(DEFINE+VARIABLE+AS+STRING+END))
            return this.parseDefineString(scriptLine);        
        if (scriptLine.matches(DEFINE+VARIABLE+AS+TILE+END))
            return this.parseDefineTile(scriptLine);
        if (scriptLine.matches(DROP+VARIABLE+END))
            return this.parseDropVariable(scriptLine);
        if (scriptLine.matches(COMMENT+ANYTHING))
            return new Return();
        return new Return(ParserState.FAILURE, "Unknown command detected");
    }
    
    private Return parseDefineInteger(String scriptLine) {
        String variableName = scriptLine.split(DEFINE)[1].split(AS)[0].trim();
        int integerValue = Integer.parseInt(scriptLine.split(AS)[1].trim());
        Return returnValue = (this.isDefinedVariable(variableName)) ? new Return(ParserState.WARNING, "Variable is already defined, previous definition dropped implicitly.") : new Return();            
        this.dropVariable(variableName);
        this.definedIntegers.put(variableName, new Integer(integerValue));
        return returnValue;
    }
    private Return parseDefineString(String scriptLine) {
        String variableName = scriptLine.split(DEFINE)[1].split(AS)[0].trim();
        String stringValue = scriptLine.split(AS)[1].trim().split(QUOTE)[1];
        Return returnValue = (this.isDefinedVariable(variableName)) ? new Return(ParserState.WARNING, "Variable is already defined, previous definition dropped implicitly.") : new Return();
        this.dropVariable(variableName);
        this.definedStrings.put(variableName, stringValue);
        return returnValue;
    }
    private Return parseDefineTile(String scriptLine) {
        String variableName = scriptLine.split(DEFINE)[1].split(AS)[0].trim();
        String[] intermediateValue = scriptLine.split(AS)[1].trim().split(SQUARE_BRACKETS)[1].split(COMMA);
        MapCoordinates tileValue = new MapCoordinates(Integer.parseInt(intermediateValue[0]), Integer.parseInt(intermediateValue[1]));
        Return returnValue = (this.isDefinedVariable(variableName)) ? new Return(ParserState.WARNING, "Variable is already defined, previous definition dropped implicitly.") : new Return();
        this.dropVariable(variableName);
        this.definedTiles.put(variableName, tileValue);
        return returnValue;
    }
    private Return parseDropVariable(String scriptLine) {
        String variableName = scriptLine.split(DROP)[1].trim();
        Return returnValue = (this.isDefinedVariable(variableName)) ? new Return() : new Return(ParserState.WARNING, "Variable is undefined, DROP statement ignored");
        this.dropVariable(variableName);
        return returnValue;
    }
}
