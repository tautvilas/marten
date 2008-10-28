package marten.age;

/*
 * HACK class to overcome multiinheritance problems
 */

public abstract class SceneGraphChildImpl implements SceneGraphChild {
    private SceneGraphParent root = null;
    
    @Override
    public SceneGraphParent getRoot() {
	return this.root;
    }
    
    @Override
    public void setRoot(SceneGraphParent newRoot) {
	this.root = newRoot;
    }
}
