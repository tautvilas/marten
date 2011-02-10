package marten.aoe.engine;

import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.TileDTO;

public abstract class TileLayer extends Tile {
    private Tile base;
    private final int detectionModifier;
    public TileLayer(String name, Tile base) {
        this (name, base, 0);
    }
    public TileLayer(String name, Tile base, int detectionModifier) {
        super(base.getName() + " " + name, base.getMap(), base.getCoordinates());
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
    @Override public final FullTileDTO getFullDTO(PlayerDTO player) {
        if (!this.isExplored(player)) {
            return new FullTileDTO(
                    "Shroud",
                    this.getCoordinates(),
                    0,
                    new MovementDTO(null),
                    new DefenseDTO(null),
                    null,
                    new String[] {"Unexplored"},
                    false
            );
        }
        for (Unit unit : this.getMap().getAllUnits(player)) {
            if (this.distanceTo(unit.getLocation()) + this.detectionModifier <= unit.getDetectionRange()) {
                return new FullTileDTO(
                        this.getName(),
                        this.getCoordinates(),
                        this.getHeight(),
                        this.getMovementCost(),
                        this.getDefenseBonus(),
                        (this.getUnit() != null && this.isVisible(player) ? this.getUnit().getFullDTO(player) : null),
                        this.getSpecialFeatures(),
                        this.isVisible(player)
                );
            }
        }
        return this.base.getFullDTO(player);
    }
    @Override
    public final TileDTO getDTO(PlayerDTO player) {
        if (!this.isExplored(player)) {
            return new TileDTO("Shroud", this.getCoordinates(), null, false);
        }
        for (Unit unit : this.getMap().getAllUnits(player)) {
            if (this.distanceTo(unit.getLocation()) + this.detectionModifier <= 0) {
                return new TileDTO(
                        this.getName(),
                        this.getCoordinates(),
                        (this.getUnit() != null && this.isVisible(player) ? this.getUnit().getDTO(player) : null),
                        this.isVisible(player)
                );
            }
        }
        return this.base.getDTO(player);
    }
    @Override public final boolean isExplored(PlayerDTO player) {
        return this.base.isExplored(player);
    }
    @Override public final boolean isVisible(PlayerDTO player) {
        return this.base.isVisible(player);
    }
    @Override public final void markAsExplored(PlayerDTO player) {
        this.base.markAsExplored(player);
    }
}
