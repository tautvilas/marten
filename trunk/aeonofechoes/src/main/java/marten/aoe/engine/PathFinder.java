package marten.aoe.engine;

import java.util.HashMap;
import java.util.List;

import marten.aoe.dto.PointDTO;

public final class PathFinder {
    private final Map map;
    private final Tile origin;
    private final HashMap<PointDTO, Path> paths = new HashMap<PointDTO, Path>();
    public PathFinder (Map map, Tile origin) {
        this.map = map;
        this.origin = origin;
        this.buildPath(new Path(origin), origin.getUnit());
    }
    private void buildPath (Path seed, Unit unit) {
        if (seed.getLength() > unit.getMovementAllowance()) {
            return;
        }
        Tile lastTile = seed.getLastTile();
        if (this.paths.containsKey(lastTile.getCoordinates())) {
            if (this.paths.get(lastTile.getCoordinates()).getLength() <= seed.getLength()) {
                return;
            }
        }
        if (lastTile.getUnit() != null && lastTile.getUnit() != unit) {
            return;
        }
        this.paths.put(lastTile.getCoordinates(), seed);
        List<Tile> extensions = lastTile.neighbors(1);
        for (Tile extension : extensions) {
            this.buildPath(seed.extend(extension, extension.getMovementCost(unit.getUnitSize(), unit.getUnitType())), unit);
        }
    }
    public Map getMap() {
        return this.map;
    }
    public Tile getOrigin() {
        return this.origin;
    }
    public List<Tile> findPathTo (Tile finish) {
        return this.paths.get(finish.getCoordinates()).getPath();
    }
}
