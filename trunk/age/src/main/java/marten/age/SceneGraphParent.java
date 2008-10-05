package marten.age;

import java.util.ArrayList;

public interface SceneGraphParent extends SceneGraphNode {
	public void addBranch (SceneGraphChild newBranch);
	public void removeBranch (SceneGraphChild oldBranch);
	public ArrayList<SceneGraphChild> getBranches ();
}
