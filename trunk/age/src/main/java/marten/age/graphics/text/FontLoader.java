package marten.age.graphics.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import marten.age.graphics.image.ImageData;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class FontLoader {

    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(FontLoader.class);

    private static final Color OPAQUE_WHITE = new Color(0xFFFFFFFF, true);
    private static final Color TRANSPARENT_BLACK = new Color(0x00000000, true);

    public static BitmapFont loadFont(Font font) {
        BufferedImage fontImage = new BufferedImage(512, 512,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) fontImage.getGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int charWidth = fm.stringWidth("W");
        int charHeight = fm.getHeight();
        int bitmapWidth = 2;
        int bitmapHeight = 2;
        while (bitmapWidth < (charWidth + 5) * 16) {
            bitmapWidth *= 2;
        }
        while (bitmapHeight < (charHeight + 5) * 16) {
            bitmapHeight *= 2;
        }

        fontImage = new BufferedImage(bitmapWidth, bitmapHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        g = (Graphics2D) fontImage.getGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(OPAQUE_WHITE);
        g.setBackground(TRANSPARENT_BLACK);
        for (int i = 0; i < 256; i++) {
            int x = i % 16;
            int y = i / 16;
            char ch[] = { (char) i };
            String temp = new String(ch);
            g.drawString(temp, (x * (bitmapWidth / 16)) + 1, (y
                    * (bitmapHeight / 16) + fm.getAscent()));
        }

        Texture texture = TextureLoader.loadTexture(new ImageData(fontImage));

        int base = GL11.glGenLists(256); // Storage For 256 Characters

        float textureDeltaX = charWidth / (float)bitmapWidth;
        float textureDeltaY = charHeight / (float)bitmapHeight;
        for (int i = 0; i < 256; i++) {
            float u = ((float) (i % 16)) / 16.0f;
            float v = 1.f - (((float) (i / 16)) / 16.0f);
            GL11.glNewList(base + i, GL11.GL_COMPILE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glNormal3d(0.0, 0.0, -1.0);
            GL11.glTexCoord2f(u, v);
            GL11.glVertex3d(0, charHeight, 0.0);
            GL11.glTexCoord2f(u, v - textureDeltaY);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glTexCoord2f((u + textureDeltaX), v - textureDeltaY);
            GL11.glVertex3d(charWidth, 0.0, 0.0);
            GL11.glTexCoord2f((u + textureDeltaX), v);
            GL11.glVertex3d(charWidth, charHeight, 0.0);
            GL11.glEnd();
            GL11.glEndList();
        }

        return new BitmapFont(base, texture, charHeight, charWidth, fm);
    }
}
