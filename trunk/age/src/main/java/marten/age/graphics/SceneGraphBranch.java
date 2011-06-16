package marten.age.graphics;

import java.util.ArrayList;

public interface SceneGraphBranch<T extends SceneGraphChild> extends SceneGraphChild {
    public void removeChildren();
    public void removeChild(T oldBranch);
    public boolean hasChild(T child);
    public ArrayList<T> getBranches();
    public void addChild(T newBranch);
    public void addChild(T newBranch, int index);
    public void updateChild(T newBranch, int index);
}
