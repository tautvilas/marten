package marten.age;

public class Branch extends SceneGraphParent {
	private SceneGraphParent root = null;

	public SceneGraphParent getRoot() {
		return this.root;
	}
	public void setRoot(SceneGraphParent newRoot) {
		this.root = newRoot;
	}
}
