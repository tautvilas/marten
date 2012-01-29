package marten.aoe.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class TileDTO implements Serializable {
    private static final long serialVersionUID = -1944860101763926870L;
    private final UnitDTO unit;
    private final List<TileLayerDTO> layers;
    private final PointDTO point;
    private final boolean visible;

    public TileDTO(List<TileLayerDTO> layers, PointDTO point, UnitDTO unit) {
        this(layers, point, unit, true);
    }

    @Deprecated
    public TileDTO(String lid, PointDTO point, UnitDTO unit) {
        this.layers = new ArrayList<TileLayerDTO>();
        this.layers.add(new TileLayerDTO(lid));
        this.unit = unit;
        this.point = point;
        this.visible = true;
    }

    @Deprecated
    public TileDTO(String[] lids, PointDTO point, UnitDTO unit) {
        this.layers = new ArrayList<TileLayerDTO>();
        for (String lid : lids) {
            this.layers.add(new TileLayerDTO(lid));
        }
        this.unit = unit;
        this.point = point;
        this.visible = true;
    }

    public TileDTO(List<TileLayerDTO> layers, PointDTO point, UnitDTO unit, boolean visible) {
        if (layers.size() == 0) {
            throw new RuntimeException("Tile must have at least one layer");
        }
        this.layers = layers;
        this.unit = unit;
        this.point = point;
        this.visible = visible;
    }

    public String getName() {
        return this.layers.get(0).getName();
    }

    public List<TileLayerDTO> getLayers() {
        return this.layers;
    }

    public String[] getLayerIds() {
        String[] ids = new String[this.layers.size()];
        for (int i = 0; i < this.layers.size(); i++) {
            ids[i] = this.layers.get(i).getName();
        }
        return ids;
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
