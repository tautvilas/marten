package marten.age.graphics;

public abstract class BasicSceneGraphChild implements SceneGraphChild {
    private BasicSceneGraphBranch<? extends SceneGraphChild> root = null;

    private boolean hidden = false;
    private String id = null;

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

    //TODO:zv:optimize, cache state
    @Override
    public boolean isHidden() {
        if (this.getRoot() != null && this.getRoot().isHidden()) {
            return true;
        } else {
            return this.hidden;
        }
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
