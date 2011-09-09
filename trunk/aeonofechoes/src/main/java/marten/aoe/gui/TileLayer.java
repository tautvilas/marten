package marten.aoe.gui;

public class TileLayer implements Comparable<TileLayer> {
    private String type;

    private String priorities[];

    public TileLayer(String type, String[] priorities) {
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
    public int compareTo(TileLayer other) {
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
