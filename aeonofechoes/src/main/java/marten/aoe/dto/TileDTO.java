package marten.aoe.dto;

import java.io.Serializable;

public final class TileDTO implements Serializable {
    private static final long serialVersionUID = -1944860101763926870L;
    private final UnitDTO unit;
    private final String[] layers;
    private final PointDTO point;
    private final boolean visible;

    public TileDTO(String[] layers, PointDTO point, UnitDTO unit) {
        this(layers, point, unit, true);
    }

    public TileDTO(String name, PointDTO point, UnitDTO unit) {
        this(new String[] {name}, point, unit, true);
    }

    public TileDTO(String name, PointDTO point, UnitDTO unit, boolean visible) {
        this(new String[] {name}, point, unit, visible);
    }

    public TileDTO(String[] layers, PointDTO point, UnitDTO unit, boolean visible) {
        if (layers.length == 0) {
            throw new RuntimeException("Tile must have at least one layer");
        }
        this.layers = layers;
        this.unit = unit;
        this.point = point;
        this.visible = visible;        
    }

    public String getName() {
        return this.layers[0];
    }

    public String[] getLayers() {
        return this.layers;
    }

    public UnitDTO getUnit() {
        return this.unit;
    }

    public PointDTO getCoordinates() {
        return this.point;
    }

    public boolean getVisibility() {
        return this.visible;
    }
}
