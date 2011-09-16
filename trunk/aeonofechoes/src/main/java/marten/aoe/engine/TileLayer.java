package marten.aoe.engine;

import java.util.HashSet;
import java.util.Set;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.TileDTO;

public abstract class TileLayer extends Tile {
    private Tile base;
    private final Set<PlayerDTO> playerDetection = new HashSet<PlayerDTO>();
    public TileLayer(String name, Tile base) {
        super(name, base.getMap(), base.getCoordinates());
        this.base = base;
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
//        TileLayer overlay = this.getOverlay();
//        if (overlay != null) {
//            overlay.setBase(this.base);
//        }
//        this.base.setOverlay(overlay);
    }
    @Override public final FullTileDTO getFullDTO(PlayerDTO player) {
        if (player == PlayerDTO.SYSTEM) {
            return new FullTileDTO(
                    this.getLayers(player),
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
        if (!this.isCloaked(player)) {
            return new FullTileDTO(
                    this.getLayers(player),
                    this.getCoordinates(),
                    this.getHeight(),
                    this.getMovementCost(),
                    this.getDefenseBonus(),
                    (this.getUnit() != null && this.isVisible(player) ? this.getUnit().getFullDTO(player) : null),
                    this.getSpecialFeatures(),
                    this.isVisible(player)
            );
        }
        for (Unit unit : this.getMap().getAllUnits(player)) {
            int distance = this.distanceTo(unit.getLocation());
            if (distance == 0 || (distance <= unit.getDetectionRange()) && unit.isObserving()) {
                return new FullTileDTO(
                        this.getLayers(player),
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
                    this.getLayers(player),
                    this.getCoordinates(),
                    (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
                    true
            );
        }
        if (!this.isExplored(player)) {
            return new TileDTO("Shroud", this.getCoordinates(), null, false);
        }
        if (!this.isCloaked(player)) {
            return new TileDTO(
                    this.getLayers(player),
                    this.getCoordinates(),
                    (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
                    this.isVisible(player)
            );
        }
        for (Unit unit : this.getMap().getAllUnits(player)) {
            int distance = this.distanceTo(unit.getLocation());
            if (distance == 0 || (distance <= unit.getDetectionRange()) && unit.isObserving()) {
                return new TileDTO(
                        this.getLayers(player),
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
    public abstract boolean isCloaked(PlayerDTO player);
    @Override public final boolean isDetected(PlayerDTO player) {
        if (this.isCloaked(player)) {
            return this.playerDetection.contains(player);
        }
        return this.base.isDetected(player);
    }
    @Override public final void setDetected(PlayerDTO player) {
        this.playerDetection.add(player);
        this.base.setDetected(player);
    }
    @Override public final void setUndetected(PlayerDTO player) {
        this.playerDetection.remove(player);
        this.base.setUndetected(player);
    }
    @Override public final boolean hasAnythingCloaked(PlayerDTO player) {
        return this.isCloaked(player) || this.base.hasAnythingCloaked(player);
    }
    @Override public final String[] getLayers(PlayerDTO player) {
        String[] layersBelow = this.base.getLayers(player);
        if (this.isCloaked(player) && !this.playerDetection.contains(player)) {
            return layersBelow;
        }
        String[] layers = new String[layersBelow.length + 1];
        for (int i = 0; i < layersBelow.length; i++) {
            layers[i] = layersBelow[i];
        }
        layers[layersBelow.length] = this.getName();
        return layers;
    }
}
