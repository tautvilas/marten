package marten.age.graphics;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BasicSceneGraphBranch<T extends SceneGraphChild> extends
        BasicSceneGraphChild implements SceneGraphBranch<T> {
    private ArrayList<T> branches = new ArrayList<T>();
    private HashMap<String, T> lookup = new HashMap<String, T>();

    @Override
    public void addChild(T newBranch) {
        newBranch.setRoot(this);
        this.branches.add(newBranch);
    }

    @Override
    public void addChild(int index, T newBranch) {
        newBranch.setRoot(this);
        this.branches.add(index, newBranch);
    }

    @Override
    public void updateChild(String id, T newBranch, int index) {
        if (this.lookup.containsKey(id)) {
            this.removeChild(lookup.get(id));
        }
        this.addChild(index, newBranch);
        this.lookup.put(id, newBranch);
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
