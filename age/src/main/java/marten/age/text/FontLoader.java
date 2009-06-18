package marten.age.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import marten.age.texture.TextureLoader;

import org.lwjgl.opengl.GL11;

public class FontLoader {

    private static final Color OPAQUE_WHITE = new Color(0xFFFFFFFF, true);
    private static final Color TRANSPARENT_BLACK = new Color(0x00000000, true);

    public static BitmapFont loadFont(Font font) {
        BufferedImage fontImage;
        int bitmapSize = 512;

        boolean sizeFound = false;

        int fontSize = 2;
        int originalSize = font.getSize();
        int fontStyle = font.getStyle();
        String fontName = font.getFontName();

        /*
         * To find out how much space a Font takes, you need to use a the
         * FontMetrics class. To get the FontMetrics, you need to get it from a
         * Graphics context. A Graphics context is only available from a
         * displayable surface, ie any class that subclasses Component or any
         * Image. First the font is set on a Graphics object. Then get the
         * FontMetrics and find out the width and height of the widest character
         * (W). Then take the largest of the 2 values and find the maximum size
         * font that will fit in the size allocated.
         */
        while (!sizeFound) {
            font = new Font(fontName, fontStyle, fontSize);
            fontImage = new BufferedImage(bitmapSize, bitmapSize,
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D) fontImage.getGraphics();
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth("W");
            int height = fm.getHeight();
            int lineWidth = (width > height) ? width * 16 : height * 16;
            if (lineWidth < bitmapSize) {
                fontSize += 2;
            } else {
                sizeFound = true;
                fontSize -= 2;
            }
        }

        /*
         * Now that a font size has been determined, create the final image, set
         * the font and draw the standard/extended ASCII character set for that
         * font.
         */
        font = new Font(fontName, fontStyle, fontSize);
        fontImage = new BufferedImage(bitmapSize, bitmapSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) fontImage.getGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(OPAQUE_WHITE);
        g.setBackground(TRANSPARENT_BLACK);
        FontMetrics fm = g.getFontMetrics();
        for (int i = 0; i < 256; i++) {
            int x = i % 16;
            int y = i / 16;
            char ch[] = { (char) i };
            String temp = new String(ch);
            g.drawString(temp, (x * 32) + 1, (y * 32) + fm.getAscent());
        }

        int texture = TextureLoader.loadTexture(fontImage).getTextureId();

        int base = GL11.glGenLists(256); // Storage For 256 Characters

        // Generate the display lists. One for each character in the
        // standard/extended ASCII chart.

        double size = originalSize;

        float textureDelta = 1.0f / 16.0f;
        for (int i = 0; i < 256; i++) {
            float u = ((float) (i % 16)) / 16.0f;
            float v = 1.f - (((float) (i / 16)) / 16.0f);
            GL11.glNewList(base + i, GL11.GL_COMPILE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glNormal3d(0.0, 0.0, -1.0);
            GL11.glTexCoord2f(u, v);
            GL11.glVertex3d(0, size, 0.0);
            GL11.glTexCoord2f(u, v - textureDelta);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glTexCoord2f((u + textureDelta), v - textureDelta);
            GL11.glVertex3d(size, 0.0, 0.0);
            GL11.glTexCoord2f((u + textureDelta), v);
            GL11.glVertex3d(size, size, 0.0);
            GL11.glEnd();
            GL11.glEndList();
        }

        return new BitmapFont(base, texture, size);
    }
}
