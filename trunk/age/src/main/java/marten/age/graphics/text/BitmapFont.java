package marten.age.graphics.text;

import java.awt.FontMetrics;

import org.lwjgl.opengl.GL11;

import marten.age.graphics.texture.Texture;

public class BitmapFont {
    private Texture texture = null;
    // font list start
    private int base = -1;
    private int size;
    private FontMetrics metrics;
    private int[] charWidths;

    public BitmapFont(int base, Texture texture, int size, int charWidth,
            FontMetrics metrics) {
        this.base = base;
        this.texture = texture;
        this.size = size;
        this.metrics = metrics;
        charWidths = metrics.getWidths();
    }

    public int getBase() {
        return this.base;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public int getCharWidth(char c) {
        return charWidths[(int)c];
    }

    public int getSize() {
        return this.size;
    }

    public int stringWidth(String string) {
        return this.metrics.stringWidth(string);
    }

    public int getMaxCharWidth() {
        return charWidths[(char)'W'];
    }

 // TODO(zv):check about glCallLists
    public void drawLetter(char c, int x, int y) {
//        GL11.glCallList(this.getBase() + c);
        int i = (int)c;
        int charHeight = this.size;
        int charWidth = this.charWidths[i];
        float u = ((float) (i % 16)) / 16.0f;
        float v = 1.f - (((float) (i / 16)) / 16.0f);
        float textureDeltaX = charWidth / this.texture.getDimension().width;
        float textureDeltaY = charHeight / this.texture.getDimension().height;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getTextureId());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3d(0.0, 0.0, -1.0);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex3d(x, charHeight + y, 0.0);
        GL11.glTexCoord2f(u, v - textureDeltaY);
        GL11.glVertex3d(x, y, 0.0);
        GL11.glTexCoord2f((u + textureDeltaX), v - textureDeltaY);
        GL11.glVertex3d(charWidth + x, y, 0.0);
        GL11.glTexCoord2f((u + textureDeltaX), v);
        GL11.glVertex3d(charWidth + x, charHeight + y, 0.0);
        GL11.glEnd();
//      IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
//      intBuffer.put(new int[] {1});
//      intBuffer.rewind();
//      GL11.glCallLists(intBuffer);
    }
}
