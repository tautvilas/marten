package marten.age.graphics.transform;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.util.Point;

import org.lwjgl.opengl.GL11;

public final class TranslationGroup extends BasicSceneGraphBranch {
    private Point coordinates = new Point();

    public TranslationGroup() {
    };

    public TranslationGroup(Point newCoordinates) {
        this.setCoordinates(newCoordinates);
    }

    public void setCoordinates(Point newCoordinates) {
        this.coordinates = new Point(newCoordinates);
    }

    public Point getCoordinates() {
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
