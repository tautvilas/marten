package marten.aoe.engine;

import marten.aoe.dto.DamageDTO;
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
        if (player == PlayerDTO.SYSTEM) {
            return new FullTileDTO(
                    this.getName(),
                    this.getCoordinates(),
                    this.getHeight(),
                    this.getMovementCost(),
                    this.getDefenseBonus(),
                    (this.getUnit() != null ? this.getUnit().getFullDTO(player) : null),
                    this.getSpecialFeatures(),
                    true
            );
        }
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
        if (player == PlayerDTO.SYSTEM) {
            return new TileDTO(
                    this.getName(),
                    this.getCoordinates(),
                    (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
                    true
            );
        }
        if (!this.isExplored(player)) {
            return new TileDTO("Shroud", this.getCoordinates(), null, false);
        }
        for (Unit unit : this.getMap().getAllUnits(player)) {
            if (this.distanceTo(unit.getLocation()) + this.detectionModifier <= 0) {
                return new TileDTO(
                        this.getName(),
                        this.getCoordinates(),
                        (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
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
    @Override public final void setExplored(PlayerDTO player) {
        this.base.setExplored(player);
    }
    @Override public final void setVisible(PlayerDTO player) {
        this.base.setVisible(player);
    }
    @Override public final void setInvisible(PlayerDTO player) {
        this.base.setInvisible(player);
    }
    @Override public final Unit getUnit() {
        return this.base.getUnit();
    }
    @Override public final Unit popUnit(PlayerDTO player) {
        Unit unit = this.base.popUnit(player);
        if (unit != null) {
            this.onUnitExit();
        }
        return unit;
    }
    @Override public final boolean pushUnit(PlayerDTO player, Unit unit) {
        boolean answer = this.base.pushUnit(player, unit);
        if (answer) {
            this.onUnitEntry();
        }
        return answer;
    }
    @Override public final Unit removeUnit(PlayerDTO player) {
        return this.base.removeUnit(player);
    }
    @Override public final boolean insertUnit(PlayerDTO player, Unit unit) {
        return this.base.insertUnit(player, unit);
    }
    @Override public final void applyDamage(DamageDTO damage) {
        this.base.applyDamage(damage);
    }
    @Override public final void turnOver() {
        this.base.turnOver();
        this.onTurnOver();
    }
    public abstract void onTurnOver();
}
