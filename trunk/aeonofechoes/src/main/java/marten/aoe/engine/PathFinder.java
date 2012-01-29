package marten.aoe.engine;

import java.util.HashMap;
import java.util.List;

import marten.aoe.GameInfo;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.PlayerDTO;
import marten.aoe.engine.core.Tile;
import marten.aoe.engine.core.Unit;

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
        PointDTO lastLocation = lastTile.getCoordinates();
        if (this.paths.containsKey(lastLocation)) {
            if (this.paths.get(lastLocation).getLength() <= seed.getLength()) {
                return;
            }
        }
        if (lastTile.isOccupied() && lastTile.getUnit() != unit) {
            return;
        }
        this.paths.put(lastLocation, seed);
        List<Tile> extensions = lastTile.neighbors(1);
        for (Tile extension : extensions) {
            int movementCost = GameInfo.calculator.getMovementCost(extension.getDTO(PlayerDTO.SYSTEM), unit.getDTO(PlayerDTO.SYSTEM));
            if (movementCost > 0) {
                this.buildPath(seed.extend(extension, movementCost), unit);
            }
        }
    }
    public Map getMap() {
        return this.map;
    }
    public Tile getOrigin() {
        return this.origin;
    }
    public List<Tile> findPathTo (Tile finish) {
        Path path = this.paths.get(finish.getCoordinates());
        if (path == null) {
            return null;
        }
        return path.getPath();
    }
}
