package marten.aoe.loader;

import java.util.ArrayList;
import java.util.List;

final class DataTree {
    private String value;
    private ArrayList<DataTree> branches = new ArrayList<DataTree>();
    public DataTree(String value) {
        this.value = value;
    }
    public void addBranch(DataTree branch) {
        this.branches.add(branch);
    }
    public String value() {
        return this.value;
    }
    public List<DataTree> branches() {
        return this.branches;
    }
}
