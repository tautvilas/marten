package marten.age.graphics.text;

import java.awt.Font;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class FontCache {
    private static org.apache.log4j.Logger log = Logger.getLogger(FontCache.class);
    private static HashMap<Font, BitmapFont> fonts = new HashMap<Font, BitmapFont>();

    public static BitmapFont loadFont(Font font) {
        BitmapFont result = null;
        if (!fonts.containsKey(font)) {
            result = FontLoader.loadFont(font);
            fonts.put(font, result);
            log.debug("FONT " + font + " added to font cache.");
        } else {
            log.debug("FONT " + font + " is allready in font cache.");
        }
        return result;
    }

    public static BitmapFont getFont(Font font) {
        if (fonts.containsKey(font)) {
            return fonts.get(font);
        } else {
            return loadFont(font);
        }
    }
}
