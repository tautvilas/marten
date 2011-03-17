package marten.age.graphics;

public abstract class BasicSceneGraphChild implements SceneGraphChild {
    private BasicSceneGraphBranch root = null;
    
    @Override
    public BasicSceneGraphBranch getRoot() {
	return this.root;
    }
    
    @Override
    public void setRoot(BasicSceneGraphBranch newRoot) {
	this.root = newRoot;
    }
}
