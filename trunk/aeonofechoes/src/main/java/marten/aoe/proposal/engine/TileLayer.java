package marten.aoe.proposal.engine;

public abstract class TileLayer extends Tile {
    private Tile base;
    public TileLayer(String name, Tile base) {
        super(base.getName() + " " + name, base.getOwner(), base.getCoordinates());
        this.base = base;
    }
    public final void setBase(Tile base) {
        this.base = base;
    }
    public final Tile getBase() {
        return this.base;
    }
    @Override public String[] getSpecialFeatures(Player player) {
        String[] baseSpecialFeatures = this.base.getSpecialFeatures(player);
        String[] layerSpecialFeatures = this.getLayerSpecificSpecialFeatures(player);
        String[] completeSpecialFeatures = new String[baseSpecialFeatures.length + layerSpecialFeatures.length];
        System.arraycopy(layerSpecialFeatures, 0, completeSpecialFeatures, 0, layerSpecialFeatures.length);
        System.arraycopy(baseSpecialFeatures, 0, completeSpecialFeatures, layerSpecialFeatures.length, baseSpecialFeatures.length);
        return completeSpecialFeatures;
    }
    public abstract String[] getLayerSpecificSpecialFeatures(Player player);
    public final void selfDestruct() {
        TileLayer overlay = this.getOverlay();
        if (overlay != null) {
            overlay.setBase(this.base);
        }
        this.base.setOverlay(overlay);
    }
}
