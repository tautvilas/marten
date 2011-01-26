package marten.aoe.proposal.engine;

import marten.aoe.proposal.dto.MinimalTileDTO;
import marten.aoe.proposal.dto.TileDTO;

public abstract class TileLayer extends Tile {
    private Tile base;
    private final int detectionModifier;
    public TileLayer(String name, Tile base) {
        this (name, base, 0);
    }
    public TileLayer(String name, Tile base, int detectionModifier) {
        super(base.getName() + " " + name, base.getOwner(), base.getCoordinates());
        this.base = base;
        this.detectionModifier = detectionModifier;
    }
    public final void setBase(Tile base) {
        this.base = base;
    }
    public final Tile getBase() {
        return this.base;
    }
    @Override public String[] getSpecialFeatures() {
        String[] baseSpecialFeatures = this.base.getSpecialFeatures();
        String[] layerSpecialFeatures = this.getLayerSpecificSpecialFeatures();
        String[] completeSpecialFeatures = new String[baseSpecialFeatures.length + layerSpecialFeatures.length];
        System.arraycopy(layerSpecialFeatures, 0, completeSpecialFeatures, 0, layerSpecialFeatures.length);
        System.arraycopy(baseSpecialFeatures, 0, completeSpecialFeatures, layerSpecialFeatures.length, baseSpecialFeatures.length);
        return completeSpecialFeatures;
    }
    public abstract String[] getLayerSpecificSpecialFeatures();
    public final void selfDestruct() {
        TileLayer overlay = this.getOverlay();
        if (overlay != null) {
            overlay.setBase(this.base);
        }
        this.base.setOverlay(overlay);
    }
    @Override public final TileDTO getDTO(Player player) {
        for (Unit unit : player.getAllUnits()) {
            if (this.getCoordinates().distanceTo(unit.getLocation().getCoordinates()) + this.detectionModifier <= 0)
                return new TileDTO(
                        this.getName(),
                        this.getCoordinates(),
                        this.getHeight(),
                        this.getMovementCost(),
                        this.getDefenseBonus(),
                        (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
                        this.getSpecialFeatures()
                );
        }
        return this.base.getDTO(player);
    }
    @Override
    public final MinimalTileDTO getMinimalDTO(Player player) {
        for (Unit unit : player.getAllUnits()) {
            if (this.getCoordinates().distanceTo(unit.getLocation().getCoordinates()) + this.detectionModifier <= 0)
                return new MinimalTileDTO(
                        this.getName(),
                        this.getCoordinates(),
                        (this.getUnit() != null ? this.getUnit().getMinimalDTO(player) : null)
                );
        }
        return this.base.getMinimalDTO(player);
    }
}
