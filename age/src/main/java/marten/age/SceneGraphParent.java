package marten.age;

import java.util.ArrayList;

public interface SceneGraphParent extends SceneGraphNode {
    public void removeBranches();
    public void removeBranch(BasicSceneGraphChild oldBranch);
    public ArrayList<BasicSceneGraphChild> getBranches();
    public void addBranch(BasicSceneGraphChild newBranch);
}
