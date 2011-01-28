package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

public final class Path {
    private final List<Tile> path;
    private final int length;
    public Path (List<Tile> path, int length) {
        this.path = path;
        this.length = length;
    }
    public Path (Tile seed) {
        this.path = new ArrayList<Tile>();
        this.path.add(seed);
        this.length = 0;
    }
    public List<Tile> getPath() {
        return this.path;
    }
    public int getLength() {
        return this.length;
    }
    public Path extend(Tile newTile, int extraLength) {
        List<Tile> newPath = new ArrayList<Tile>(this.path);
        newPath.add(newTile);
        return new Path(newPath, this.length + extraLength);
    }
    public Tile getLastTile() {
        return this.path.get(this.path.size() - 1);
    }
}
