package marten.age.graphics.geometry;

import org.lwjgl.opengl.GL11;

public abstract class GeneratedGeometry implements Geometry {
    private int list = 0;

    abstract public void generate();

    public void setList(int list) {
        this.list = list;
    }

    public void draw() {
        if (list == 0) {
            this.generate();
        }
        GL11.glCallList(this.list);
    }
}
