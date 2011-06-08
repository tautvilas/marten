package marten.age.graphics;

public interface SceneGraphChild extends SceneGraphNode {
    public BasicSceneGraphBranch<? extends SceneGraphChild> getRoot();

    public void setRoot(BasicSceneGraphBranch<? extends SceneGraphChild> newRoot);

    public void hide();

    public void show();

    public boolean isHidden();
}
