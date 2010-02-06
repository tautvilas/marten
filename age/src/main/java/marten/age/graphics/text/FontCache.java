package marten.age.graphics.text;

import java.awt.Font;

public class FontCache {
    private static boolean cacheGenerated = false;

    public static final String COURIER_BOLD_20 = "courierBold20";
    public static final String COURIER_14 = "courier14";

    private static BitmapFont CourierBold20;
    private static BitmapFont Courier14;

    public static void generate() {
        Font font = new Font("Courier New", Font.BOLD, 20);
        CourierBold20 = FontLoader.loadFont(font);
        font = new Font("Courier New", Font.PLAIN, 14);
        Courier14 = FontLoader.loadFont(font);

        cacheGenerated = true;
    }

    public static BitmapFont getFont(String fontId) {
        if (!cacheGenerated)
            FontCache.generate();

        if (fontId.equals(COURIER_BOLD_20)) {
            return CourierBold20;
        } else if (fontId.equals(COURIER_14)) {
            return Courier14;
        } else {
            throw new RuntimeException("No such font exists - " + fontId);
        }
    }
}
