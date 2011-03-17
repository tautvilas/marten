package marten.age.graphics.transform;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.layout.Placeable;
import marten.age.graphics.primitives.Point;

import org.lwjgl.opengl.GL11;

public class TranslationGroup extends BasicSceneGraphBranch<SceneGraphChild> implements
        Placeable {
    private Point coordinates = new Point();

    public TranslationGroup() {
    };

    public TranslationGroup(Point position) {
        this.setPosition(position);
    }

    public void setPosition(Point position) {
        this.coordinates = new Point(position);
    }

    public Point getPosition() {
        return new Point(this.coordinates);
    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        GL11.glTranslated(this.coordinates.x, this.coordinates.y,
                this.coordinates.z);
        super.render();
        GL11.glPopMatrix();
    }
}
