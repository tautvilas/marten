package marten.age;

import java.util.ArrayList;

public abstract class SceneGraphParent implements SceneGraphNode {
    private ArrayList<SceneGraphChild> branches = new ArrayList<SceneGraphChild>();
    
    public void addBranch(SceneGraphChild newBranch) {
	newBranch.setRoot(this);
	this.branches.add(newBranch);
    }
    
    public ArrayList<SceneGraphChild> getBranches() {
	return this.branches;
    }
    
    public void removeBranch(SceneGraphChild oldBranch) {
	this.branches.remove(oldBranch);
    }
    
    public void removeBranches() {
    	branches.clear();
    }
    
    public void activate() {
	activateChildren();
    }
    
    public void activateChildren() {
	if (this.branches.isEmpty())
		throw new RuntimeException ("Attempted to activate childless parent.");
	for (SceneGraphChild branch : this.branches)
		branch.activate();
    }
}
