package marten.age.graphics;

import java.util.ArrayList;

public interface SceneGraphParent extends SceneGraphNode {
    public void removeChildren();
    public void removeChild(SceneGraphChild oldBranch);
    public boolean hasChild(SceneGraphChild child);
    public ArrayList<SceneGraphChild> getBranches();
    public void addChild(SceneGraphChild newBranch);
}
