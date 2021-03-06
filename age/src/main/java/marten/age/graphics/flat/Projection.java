package marten.age.graphics.flat;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import marten.age.graphics.primitives.Point;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class Projection {
    public static Point unproject(Point windowCoords) {
        float[] position = new float[3];
        Project.gluUnProject((float) windowCoords.x,
                (float) windowCoords.y,
                (float) windowCoords.z,
                getModelviewMatrix(),
                getProjectionMatrix(),
                getViewport(),
                FloatBuffer.wrap(position));
        return new Point(position[0], position[1], position[2]);
    }

    private static FloatBuffer getModelviewMatrix() {
        float[][] modelViewMatrix = new float[4][4];
        FloatBuffer bufferModelviewMatrix = FloatBuffer.allocate(16);
        bufferModelviewMatrix.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, bufferModelviewMatrix);
        bufferToArray(bufferModelviewMatrix, modelViewMatrix);
//        return modelViewMatrix;
        return bufferModelviewMatrix;
    }

    private static FloatBuffer getProjectionMatrix() {
        float[][] projectionMatrix = new float[4][4];
        FloatBuffer bufferProjectionMatrix = FloatBuffer.allocate(16);
        bufferProjectionMatrix.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, bufferProjectionMatrix);
        bufferToArray(bufferProjectionMatrix, projectionMatrix);
//        return projectionMatrix;
        return bufferProjectionMatrix;
    }

    private static IntBuffer getViewport() {
        IntBuffer bufferViewport = IntBuffer.allocate(16);
        bufferViewport.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, bufferViewport);
//        return bufferViewport.array();
        return bufferViewport;
    }

    private static void bufferToArray(FloatBuffer fb, float[][] fa) {
        for (int i = 0; i < fa.length; i++) {
            for (int j = 0; j < fa[i].length; j++) {
                fa[i][j] = fb.get();
            }
        }
    }
}
