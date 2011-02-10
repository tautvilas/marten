package marten.aoe.dto;

import java.io.Serializable;

public final class TileDTO implements Serializable {
    private static final long serialVersionUID = -1944860101763926870L;
    private final UnitDTO unit;
    private final String name;
    private final PointDTO point;
    private final boolean visible;
    public TileDTO (String name, PointDTO point, UnitDTO unit, boolean visible) {
        this.name = name;
        this.unit = unit;
        this.point = point;
        this.visible = visible;
    }
    public String getName () {
        return this.name;
    }
    public UnitDTO getUnit () {
        return this.unit;
    }
    public PointDTO getCoordinates () {
        return this.point;
    }
    public boolean getVisibility () {
        return this.visible;
    }
}
