package marten.aoe.engine;

public class Unit {
    private TileCoordinate currentLocation;
    private int maxMovement;
    private int availableMovement;
    private UnitType type;
    private PathFinder pathFinder;
    private Unit (TileCoordinate at, UnitType type, int movement) {
        this.currentLocation = at;
        this.maxMovement = this.availableMovement = movement;
        this.type = type;
        this.pathFinder = new PathFinder(this.currentLocation, this.type, this.availableMovement);
    }
    public void onEndTurn() {
        this.availableMovement = this.maxMovement;
        this.pathFinder = new PathFinder(this.currentLocation, this.type, this.availableMovement);
    }
    public int availableMovement() {
        return this.availableMovement;
    }
    public void move(TileCoordinate to) {        
        TilePath path = this.pathFinder.findPath(to);
        if (path == null)
            return;
        TileCoordinate prevLocation = this.currentLocation;
        for (TileCoordinate nextLocation : path.tiles()) {
            TileMap.get(prevLocation).exit();
            TileMap.get(nextLocation).enter(this);
            prevLocation = nextLocation;
        }
        this.availableMovement -= path.length();
        this.currentLocation = path.endPoint();
        this.pathFinder = new PathFinder(this.currentLocation, this.type, this.availableMovement);
    }
}