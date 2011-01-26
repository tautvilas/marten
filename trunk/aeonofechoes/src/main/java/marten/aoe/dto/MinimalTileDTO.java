package marten.aoe.dto;

import java.io.Serializable;

public final class MinimalTileDTO implements Serializable {
    private static final long serialVersionUID = -1944860101763926870L;
    private final MinimalUnitDTO unit;
    private final String name;
    private final PointDTO point;
    public MinimalTileDTO (String name, PointDTO point, MinimalUnitDTO unit) {
        this.name = name;
        this.unit = unit;
        this.point = point;
    }
    public String getName () {
        return this.name;
    }
    public MinimalUnitDTO getUnit () {
        return this.unit;
    }
    public PointDTO getCoordinates () {
        return this.point;
    }
}
