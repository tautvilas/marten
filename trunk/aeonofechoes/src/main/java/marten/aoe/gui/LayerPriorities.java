package marten.aoe.gui;

public class LayerPriorities implements Comparable<LayerPriorities> {
    private String type;

    private String priorities[];

    public LayerPriorities(String type, String[] priorities) {
        super();
        this.type = type;
        this.priorities = priorities;
    }

    public String getType() {
        return type;
    }

    public String[] getPriorities() {
        return priorities;
    }

    @Override
    public int compareTo(LayerPriorities other) {
        for (int i = 0; i < this.priorities.length; i++) {
            if (i >= other.getPriorities().length)
                break;
            int compare = this.priorities[i]
                    .compareTo(other.getPriorities()[i]);
            if (compare != 0)
                return compare;
        }
        return this.type.compareTo(other.getType());
    }
}
