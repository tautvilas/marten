package marten.age.graphics.text;

import java.util.HashMap;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.Sprite;
import marten.age.graphics.primitives.Point;

import org.lwjgl.opengl.GL11;

public class BitmapString extends Sprite {
    private BitmapFont font;
    private String content = "";
    private int numLines = 0;
    private HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
    private Point position = new Point();

    public BitmapString(BitmapFont font) {
        this.colors.put(new Integer(0), new Color(1.0, 1.0, 1.0));
        this.font = font;
    }

    public BitmapString(BitmapFont font, String content) {
        this(font);
        this.setContent(content);
    }

    public BitmapString(BitmapFont font, String content, Color color) {
        this(font, content);
        this.colors.put(new Integer(0), color);
    }

    public void setContent(String content) {
        this.content = content;
        this.numLines = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                this.numLines++;
            }
        }
    }

    public void addContent(String content) {
        this.setContent(this.content + content);
    }

    // TODO(zv):check about glCallLists
    private void glPrint(String msg) {
        if (msg != null) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            GL11.glTranslated(this.position.x, this.position.y + this.numLines
                    * this.font.getSize(), 0);
            int lineTranslate = 0;
            for (int i = 0; i < msg.length(); i++) {
                Integer index = new Integer(i);
                if (colors.containsKey(index)) {
                    Color color = colors.get(index);
                    GL11.glColor3d(color.r, color.g, color.b);
                }
                if (msg.charAt(i) == '\n') {
                    GL11.glTranslated(-lineTranslate, -font.getSize(), 0.0);
                    lineTranslate = 0;
                } else {
                    int width = font.getMetrics().charWidth(msg.charAt(i));
                    GL11.glCallList(font.getBase() + msg.charAt(i));
                    GL11.glTranslated(width, 0.0f, 0.0f);
                    lineTranslate += width;
                }
            }
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        } else {
            throw new RuntimeException("The value for the message was not set.");
        }
    }

    public void setColor(Color color) {
        this.colors.put(new Integer(0), color);
    }

    public void addColor(Color color) {
        this.colors.put(new Integer(this.content.length()), color);
    }

    public void render() {
        glPrint(this.content);
    }

    @Override
    public int getHeight() {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public int getWidth() {
        return font.getMetrics().stringWidth(this.content);
    }

    @Override
    public void setPosition(Point point) {
        this.position = point;
    }
}
