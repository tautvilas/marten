package marten.age.graphics.text;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.util.Color;

import org.lwjgl.opengl.GL11;

public class BitmapString extends BasicSceneGraphChild {
    private BitmapFont font;
    private String content;
    private Color color = new Color(255, 255, 255);

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
}