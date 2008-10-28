package marten.age;

public abstract class SceneGraphChild implements SceneGraphNode {
    private SceneGraphParent root = null;
    
    public SceneGraphParent getRoot() {
	return this.root;
    }
    
    public void setRoot(SceneGraphParent newRoot) {
	this.root = newRoot;
    }

    @Override
    public abstract void activate();
}
