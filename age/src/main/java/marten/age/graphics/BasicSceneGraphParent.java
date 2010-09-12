package marten.age.graphics;

import java.util.ArrayList;

public abstract class BasicSceneGraphParent implements SceneGraphParent {
    private ArrayList<SceneGraphChild> branches = new ArrayList<SceneGraphChild>();

    @Override
    public void addChild(SceneGraphChild newBranch) {
        newBranch.setRoot(this);
        this.branches.add(newBranch);
    }

    @Override
    public ArrayList<SceneGraphChild> getBranches() {
        return this.branches;
    }

    @Override
    public void removeChild(SceneGraphChild oldBranch) {
        this.branches.remove(oldBranch);
    }

    @Override
    public boolean hasChild(SceneGraphChild child) {
        return branches.contains(child);
    }

    @Override
    public void removeChildren() {
        branches.clear();
    }

    @Override
    public void render() {
        activateChildren();
    }

    public void activateChildren() {
        if (this.branches.isEmpty())
            throw new RuntimeException(
                    "Attempted to activate childless parent.");
        for (SceneGraphChild branch : this.branches)
            branch.render();
    }
}
