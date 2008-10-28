package marten.age;

import java.util.ArrayList;

public abstract class BasicSceneGraphParent implements SceneGraphParent {
    private ArrayList<BasicSceneGraphChild> branches = new ArrayList<BasicSceneGraphChild>();
    
    @Override
    public void addBranch(BasicSceneGraphChild newBranch) {
	newBranch.setRoot(this);
	this.branches.add(newBranch);
    }
    
    @Override
    public ArrayList<BasicSceneGraphChild> getBranches() {
	return this.branches;
    }
    
    @Override
    public void removeBranch(BasicSceneGraphChild oldBranch) {
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
	for (BasicSceneGraphChild branch : this.branches)
		branch.activate();
    }
}
