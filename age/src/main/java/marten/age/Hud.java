package marten.age;

import java.util.ArrayList;

import marten.util.Point;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


public class Hud {
	private ArrayList<Point> positions = new ArrayList<Point>();
	private ArrayList<SceneGraphNode> nodes = new ArrayList<SceneGraphNode>();
	
	public void add(SceneGraphNode node, int x, int y) {
		this.positions.add(new Point(x, y, 0));
		this.nodes.add(node);
	}
	
	public void remove(SceneGraphNode node) {
		int index = nodes.indexOf(node);
		if (index != -1) {
			this.nodes.remove(node);
			positions.remove(index);
		}
	}
	
	public void display() {
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		
		boolean lightingEnabled = false;
		boolean blendEnabled = false;
		boolean depthEnabled = false;
		if (GL11.glIsEnabled(GL11.GL_LIGHTING)) lightingEnabled = true;
		if (GL11.glIsEnabled(GL11.GL_DEPTH_TEST)) depthEnabled = true;
		if (GL11.glIsEnabled(GL11.GL_BLEND)) blendEnabled = true;
		
//		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
//		GLU.gluOrtho2D(0, width, 0, height);
		GL11.glOrtho(0, width, height, 0, -1, 1);
		GL11.glScalef(1, -1, 1);
		GL11.glTranslatef(0, -height, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		for (int i = 0; i < nodes.size(); i++) {
			GL11.glPushMatrix();
			GL11.glTranslated(positions.get(i).x, positions.get(i).y, 0);
			nodes.get(i).activate();
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		if (lightingEnabled) GL11.glEnable(GL11.GL_LIGHTING);
		if (depthEnabled) GL11.glEnable(GL11.GL_DEPTH_TEST);
		if (!blendEnabled) GL11.glDisable(GL11.GL_BLEND);
	}
}
