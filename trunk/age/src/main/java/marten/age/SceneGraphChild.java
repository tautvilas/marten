package marten.age;

public interface SceneGraphChild extends SceneGraphNode {
    public SceneGraphParent getRoot();
    
    public void setRoot(SceneGraphParent newRoot);
}
