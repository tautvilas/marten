package marten.aoe.proposal.engine;

public abstract class TileLayer extends Tile {
    private final Tile base;
    public TileLayer(String name, Tile base) {
        super(base.getName() + " " + name, base.getOwner(), base.getCoordinates());
        this.base = base;
    }
    public Tile getBase() {
        return this.base;
    }
}
