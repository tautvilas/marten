package marten.age.text;

import marten.age.BasicSceneGraphChild;
import marten.age.util.Color;

import org.lwjgl.opengl.GL11;

public class BitmapString extends BasicSceneGraphChild {
    private BitmapFont font;
    private String content;
    private Color color = new Color(255, 255, 255);

    public BitmapString(BitmapFont font, String content) {
        this.font = font;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // TODO(zv):check about glCallLists
    private void glPrint(String msg) {
        if (msg != null) {
            boolean lighting = false;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            if (GL11.glIsEnabled(GL11.GL_LIGHTING)) {
                lighting = true;
                GL11.glDisable(GL11.GL_LIGHTING);
            }
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
            if (lighting) {
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void render() {
        glPrint(this.content);
    }
}
