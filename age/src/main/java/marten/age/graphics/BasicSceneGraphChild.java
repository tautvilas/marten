package marten.age.graphics;

public abstract class BasicSceneGraphChild implements SceneGraphChild {
    private BasicSceneGraphBranch<? extends SceneGraphChild> root = null;
    
    @Override
    public BasicSceneGraphBranch<? extends SceneGraphChild> getRoot() {
	return this.root;
    }
    
    @Override
    public void setRoot(BasicSceneGraphBranch<? extends SceneGraphChild> newRoot) {
	this.root = newRoot;
    }
}
