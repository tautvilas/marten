package marten.aoe.engine;

public final class Unit {
    private TileMap tileMap;
    private Tile currentLocation;
    private TileCoordinate at;
    private String profileName;
    private int maxMovement;
    private int availableMovement;
    private UnitType type;
    private PathFinder pathFinder;
    public Unit (TileMap tileMap, TileCoordinate at, UnitProfile profile) {
        this.tileMap = tileMap;
        this.at = at;
        this.currentLocation = tileMap.get(at);
        this.currentLocation.enter(this);
        this.maxMovement = this.availableMovement = profile.maxMovement();
        this.type = profile.type();
        this.profileName = profile.name();
        this.pathFinder = new PathFinder(this.tileMap, this.at, this.type, this.availableMovement);
    }
    public void onEndTurn() {
        this.availableMovement = this.maxMovement;
        this.pathFinder = new PathFinder(this.tileMap, this.at, this.type, this.availableMovement);
    }
    public int availableMovement() {
        return this.availableMovement;
    }
    public String profileName() {
        return this.profileName;
    }
    public void move(TileCoordinate to) {        
        TilePath path = this.pathFinder.findPath(to);
        if (path == null)
            return;
        Tile prevLocation = this.currentLocation;
        for (Tile nextLocation : path.tiles()) {
            prevLocation.exit();
            nextLocation.enter(this);
            prevLocation = nextLocation;
        }
        this.availableMovement -= path.length();
        this.at = path.endPoint();
        this.currentLocation = this.tileMap.get(this.at);
        this.pathFinder = new PathFinder(this.tileMap, this.at, this.type, this.availableMovement);
    }
}
