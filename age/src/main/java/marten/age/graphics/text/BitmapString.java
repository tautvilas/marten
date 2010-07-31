package marten.age.graphics.text;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.Sprite;
import marten.age.graphics.primitives.Point;

import org.lwjgl.opengl.GL11;

public class BitmapString extends Sprite {
    private BitmapFont font;
    private String content;
    private Color color = new Color(255, 255, 255);
    private Point position = new Point();

    public BitmapString(BitmapFont font, String content) {
        this.font = font;
        this.content = content;
    }

    public BitmapString(BitmapFont font, String content, Color color) {
        this(font, content);
        this.color = color;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // TODO(zv):check about glCallLists
    private void glPrint(String msg) {
        if (msg != null) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            GL11.glColor3d(color.r, color.g, color.b);
            GL11.glTranslated(this.position.x, this.position.y, 0);
            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) == '\n') {
                    GL11.glTranslated(0.0, -font.getSize(), 0.0);
                } else {
                    GL11.glCallList(font.getBase() + msg.charAt(i));
                    GL11.glTranslated(font.getSize(), 0.0f, 0.0f);
                }
            }
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        } else {
            throw new RuntimeException("The value for the message was not set.");
        }
    }

    public void setColor(Color c) {
        this.color = c;
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
        return (int)font.getSize() * content.length();
    }

    @Override
    public void setPosition(Point point) {
        this.position = point;
    }
}
