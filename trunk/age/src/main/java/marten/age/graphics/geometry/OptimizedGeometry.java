package marten.age.graphics.geometry;

import org.lwjgl.opengl.GL11;

public final class OptimizedGeometry extends GeneratedGeometry {
    private Geometry parentGeometry = null;

    public OptimizedGeometry(Geometry newParent) {
        this.setParentGeometry(newParent);
    }

    public void setParentGeometry(Geometry newParent) {
        if (newParent instanceof GeneratedGeometry)
            throw new RuntimeException(
                    "Generated geometry is already optimized internally.");
        this.parentGeometry = newParent;
    }

    public Geometry getParentGeometry() {
        return this.parentGeometry;
    }

    @Override
    public void generate() {
        if (this.parentGeometry == null)
            throw new RuntimeException("Parent geometry not set.");
        int answer = GL11.glGenLists(1);
        if (answer == 0)
            throw new RuntimeException("Out of graphics memory.");
        GL11.glNewList(answer, GL11.GL_COMPILE);
        parentGeometry.render();
        GL11.glEndList();
        this.setList(answer);
    }

    @Override
    public boolean equals(Object o) {
        return parentGeometry.equals(o);
    }
}
