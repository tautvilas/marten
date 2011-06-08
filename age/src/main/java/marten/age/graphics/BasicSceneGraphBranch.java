package marten.age.graphics;

import java.util.ArrayList;

public abstract class BasicSceneGraphBranch<T extends SceneGraphChild> extends
        BasicSceneGraphChild implements SceneGraphBranch<T> {
    private ArrayList<T> branches = new ArrayList<T>();

    @Override
    public void addChild(T newBranch) {
        newBranch.setRoot(this);
        this.branches.add(newBranch);
    }

    @Override
    public ArrayList<T> getBranches() {
        return this.branches;
    }

    @Override
    public void removeChild(T oldBranch) {
        this.branches.remove(oldBranch);
    }

    @Override
    public boolean hasChild(T child) {
        return branches.contains(child);
    }

    @Override
    public void removeChildren() {
        branches.clear();
    }

    @Override
    public void render() {
        if (!this.isHidden()) {
            activateChildren();
        }
    }

    public void activateChildren() {
        for (T branch : this.branches)
            branch.render();
    }
}
