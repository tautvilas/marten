package marten.age.geometry;

import java.util.ArrayList;

import marten.age.util.Triangle;
import marten.age.util.Vertex;

import org.lwjgl.opengl.GL11;

public final class TriangularGeometry extends GeneratedGeometry {
    ArrayList<Triangle> triangles = new ArrayList<Triangle>();

    public TriangularGeometry(ArrayList<Triangle> model) {
        triangles = model;
    }

    public int generate() {
        if (this.triangles == null)
            throw new RuntimeException("Parent geometry not set.");
        int answer = GL11.glGenLists(1);
        if (answer == 0)
            throw new RuntimeException("Out of graphics memory.");
        GL11.glNewList(answer, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_TRIANGLES);
        for (Triangle triangle : triangles)
            for (Vertex vertex : triangle.getVertices()) {
                GL11.glTexCoord3d(vertex.textureCoordinates.r,
                        vertex.textureCoordinates.s,
                        vertex.textureCoordinates.t);
                GL11.glNormal3d(vertex.normal.x, vertex.normal.y,
                        vertex.normal.z);
                GL11.glVertex3d(vertex.coordinates.x, vertex.coordinates.y,
                        vertex.coordinates.z);
            }
        GL11.glEnd();
        GL11.glEndList();
        return answer;
    }
}
