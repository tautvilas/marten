package marten.age.root;

import java.util.ArrayList;

import marten.age.BasicSceneGraphChild;
import marten.age.SceneGraphNode;
import marten.age.BasicSceneGraphParent;
import marten.age.geometry.Geometry;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.ReusedGeometry;
import marten.age.geometry.SimpleModel;
import marten.age.model.ComplexModel;

import org.apache.log4j.Logger;

public abstract class Root extends BasicSceneGraphParent {
	private static org.apache.log4j.Logger log = Logger.getLogger(Root.class);

	public void compile() {
		compileSceneParent(this);
	}
	
	private void compileSceneParent(BasicSceneGraphParent parent) {
		ArrayList<BasicSceneGraphChild> parentBranches = parent.getBranches();
		for (SceneGraphNode child : parentBranches) {
			if (child instanceof BasicSceneGraphParent) {
				compileSceneParent((BasicSceneGraphParent)child);
			} else if (child instanceof BasicSceneGraphChild) {
				compileSceneNode(child);
			}
		}
	}
	
	private void compileSceneNode(SceneGraphNode node) {
		if (node instanceof SimpleModel) {
			SimpleModel sm = (SimpleModel)node;
			Geometry g = sm.getGeometry();
			sm.setGeometry(optimizeGeometry(g));
		} else if (node instanceof ComplexModel) {
			ComplexModel cm = (ComplexModel)node;
			ArrayList<SimpleModel> parts = cm.getParts();
			for (SimpleModel part : parts) {
				part.setGeometry(optimizeGeometry(part.getGeometry()));
			}
		}
	}
	
	private ReusedGeometry optimizeGeometry(Geometry g) {
		if (!(g instanceof ReusedGeometry)) {
			if (!(g instanceof OptimizedGeometry)) {
				log.debug("Optimizing geometry " + g);
				g = new OptimizedGeometry(g);
				((OptimizedGeometry)g).generate();
			}
			g = new ReusedGeometry(g);
		}
		return (ReusedGeometry)g;
	}
}
