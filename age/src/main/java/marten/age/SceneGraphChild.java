package marten.age;

public interface SceneGraphChild extends SceneGraphNode {
    public BasicSceneGraphParent getRoot();
    
    public void setRoot(BasicSceneGraphParent newRoot);
}
