package marten.aoe.proposal.engine;

public abstract class TileLayer extends Tile {
    private final Tile base;
    private final String[] specialFeatures;
    public TileLayer(String name, Tile base, String[] specialFeatures) {
        super(base.getName() + " " + name, base.getOwner(), base.getCoordinates());
        this.specialFeatures = specialFeatures;
        this.base = base;
    }
    public Tile getBase() {
        return this.base;
    }
    @Override public String[] getSpecialFeatures() {
        String[] baseSpecialFeatures = this.base.getSpecialFeatures();
        String[] completeSpecialFeatures = new String[this.specialFeatures.length + baseSpecialFeatures.length];
        System.arraycopy(this.specialFeatures, 0, completeSpecialFeatures, 0, this.specialFeatures.length);
        System.arraycopy(baseSpecialFeatures, 0, completeSpecialFeatures, this.specialFeatures.length, baseSpecialFeatures.length);
        return completeSpecialFeatures;
    }
}
