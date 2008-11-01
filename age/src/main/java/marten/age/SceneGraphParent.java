package marten.age;

import java.util.ArrayList;

public interface SceneGraphParent extends SceneGraphNode {
    public void removeBranches();
    public void removeBranch(SceneGraphChild oldBranch);
    public ArrayList<SceneGraphChild> getBranches();
    public void addBranch(SceneGraphChild newBranch);
}
