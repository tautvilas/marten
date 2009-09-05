package marten.age;

import java.util.ArrayList;

public interface SceneGraphParent extends SceneGraphNode {
    public void removeChildren();
    public void removeChild(SceneGraphChild oldBranch);
    public ArrayList<SceneGraphChild> getBranches();
    public void addChild(SceneGraphChild newBranch);
}