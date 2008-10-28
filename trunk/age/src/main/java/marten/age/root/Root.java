package marten.age.root;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import marten.age.SceneGraphChild;
import marten.age.SceneGraphNode;
import marten.age.SceneGraphParent;
import marten.age.Visual;
import marten.age.geometry.Geometry;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.ReusedGeometry;
import marten.age.geometry.SimpleModel;
import marten.age.model.ComplexModel;

public abstract class Root implements SceneGraphNode, SceneGraphParent {
	private static org.apache.log4j.Logger log = Logger.getLogger(Root.class);
	private ArrayList<SceneGraphChild> branches = new ArrayList<SceneGraphChild>();
	
	public void addBranch(SceneGraphChild newBranch) {
		branches.add(newBranch);
	}

	public ArrayList<SceneGraphChild> getBranches() {
		return branches;
	}

	public void removeBranch(SceneGraphChild oldBranch) {
		branches.remove(oldBranch);
	}
	
	public void removeBranches() {
		branches.clear();
	}

	public void activate() {
//		if (this.branches.isEmpty())
//			throw new RuntimeException ("Attempted to activate root without any childs.");
		for (SceneGraphChild branch : this.branches) {
			branch.activate();
		}
	}
	
	public void compile() {
		compileSceneParent(this);
	}
	
	private void compileSceneParent(SceneGraphParent parent) {
		ArrayList<SceneGraphChild> parentBranches = parent.getBranches();
		for (SceneGraphChild child : parentBranches) {
			if (child instanceof Visual) {
				child = ((Visual)child).getSceneGraph();
			}
			if (child instanceof SceneGraphParent) {
				compileSceneParent((SceneGraphParent)child);
			} else if (child instanceof SceneGraphChild) {
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
