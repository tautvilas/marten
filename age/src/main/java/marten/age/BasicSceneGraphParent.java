package marten.age;

import java.util.ArrayList;

public abstract class BasicSceneGraphParent implements SceneGraphParent {
    private ArrayList<SceneGraphChild> branches = new ArrayList<SceneGraphChild>();
    
    @Override
    public void addBranch(SceneGraphChild newBranch) {
	newBranch.setRoot(this);
	this.branches.add(newBranch);
    }
    
    @Override
    public ArrayList<SceneGraphChild> getBranches() {
	return this.branches;
    }
    
    @Override
    public void removeBranch(SceneGraphChild oldBranch) {
	this.branches.remove(oldBranch);
    }
    
    @Override
    public void removeBranches() {
    	branches.clear();
    }
    
    @Override
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
