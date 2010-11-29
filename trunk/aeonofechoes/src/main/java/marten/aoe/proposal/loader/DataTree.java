package marten.aoe.proposal.loader;

import java.util.ArrayList;
import java.util.List;

public final class DataTree {
    private final String value;
    private final ArrayList<DataTree> branches = new ArrayList<DataTree>();
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
