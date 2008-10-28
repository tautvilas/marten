package marten.age;

public class SceneGraphBranch extends SceneGraphParent implements SceneGraphChild {
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
