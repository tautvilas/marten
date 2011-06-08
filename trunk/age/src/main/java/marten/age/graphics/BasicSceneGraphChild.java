package marten.age.graphics;

public abstract class BasicSceneGraphChild implements SceneGraphChild {
    private BasicSceneGraphBranch<? extends SceneGraphChild> root = null;

    private boolean hidden = false;

    @Override
    public BasicSceneGraphBranch<? extends SceneGraphChild> getRoot() {
	return this.root;
    }
    
    @Override
    public void setRoot(BasicSceneGraphBranch<? extends SceneGraphChild> newRoot) {
	this.root = newRoot;
    }

    @Override
    public void show() {
        this.hidden = false;
    }

    @Override
    public void hide() {
        this.hidden = true;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }
}
