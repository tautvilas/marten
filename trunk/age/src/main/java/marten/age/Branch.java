package marten.age;

import java.util.ArrayList;

public abstract class Branch implements SceneGraphNode, SceneGraphParent, SceneGraphChild {
	private SceneGraphParent root = null;
	private ArrayList<SceneGraphChild> branches = new ArrayList<SceneGraphChild>();
	public void activate() {
		if (this.branches.isEmpty())
			throw new RuntimeException ("Attempted to activate leafless branch.");
		for (SceneGraphChild branch : this.branches)
			branch.activate();
	}
	public void addBranch(SceneGraphChild newBranch) {
		this.branches.add(newBranch);
	}
	public ArrayList<SceneGraphChild> getBranches() {
		return this.branches;
	}
	public void removeBranch(SceneGraphChild oldBranch) {
		this.branches.remove(oldBranch);
	}
	public SceneGraphParent getRoot() {
		return this.root;
	}
	public void setRoot(SceneGraphParent newRoot) {
		this.root = newRoot;
	}
}
