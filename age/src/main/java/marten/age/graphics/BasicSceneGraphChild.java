package marten.age.graphics;

public abstract class BasicSceneGraphChild implements SceneGraphChild {
    private BasicSceneGraphParent root = null;
    
    @Override
    public BasicSceneGraphParent getRoot() {
	return this.root;
    }
    
    @Override
    public void setRoot(BasicSceneGraphParent newRoot) {
	this.root = newRoot;
    }
}
