package marten.age.graphics;

public interface SceneGraphChild extends SceneGraphNode {
    public BasicSceneGraphBranch getRoot();

    public void setRoot(BasicSceneGraphBranch newRoot);
}
