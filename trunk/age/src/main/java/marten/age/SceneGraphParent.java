package marten.age;

import java.util.ArrayList;

public abstract class SceneGraphParent implements SceneGraphNode {
    private ArrayList<SceneGraphChildImpl> branches = new ArrayList<SceneGraphChildImpl>();
    
    public void addBranch(SceneGraphChildImpl newBranch) {
	newBranch.setRoot(this);
	this.branches.add(newBranch);
    }
    
    public ArrayList<SceneGraphChildImpl> getBranches() {
	return this.branches;
    }
    
    public void removeBranch(SceneGraphChildImpl oldBranch) {
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
	for (SceneGraphChildImpl branch : this.branches)
		branch.activate();
    }
}
