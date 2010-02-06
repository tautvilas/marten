package marten.age.graphics;

public class BasicSceneGraphBranch extends BasicSceneGraphParent implements SceneGraphBranch {
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
