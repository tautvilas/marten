package marten.aoe.loader;

import java.util.ArrayList;
import java.util.List;

/** A simple implementation of a data tree structure, where values are held as strings.
 * @author Petras Ražanskas*/
final class DataTree {
    private String value;
    private ArrayList<DataTree> branches = new ArrayList<DataTree>();
    /** Creates a data tree node with the given value
     * @param value the value to be held at this node */
    public DataTree(String value) {
        this.value = value;
    }
    /** Appends the given data tree to this node.
     * @param branch the data tree to be appended to this node*/
    public void addBranch(DataTree branch) {
        this.branches.add(branch);
    }
    /** @return the value of this node */
    public String value() {
        return this.value;
    }
    /** @return the list of subtrees originating from this node.*/
    public List<DataTree> branches() {
        return this.branches;
    }
}
