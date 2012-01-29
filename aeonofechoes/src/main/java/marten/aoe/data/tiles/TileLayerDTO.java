package marten.aoe.data.tiles;

import java.io.Serializable;

import marten.aoe.data.type.TileType;

public class TileLayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private TileType type;
    private int groundMovementCost;
    private int defence = 2;
    public int getDefence() {
        return defence;
    }

    protected void setDefence(int defence) {
        this.defence = defence;
    }

    private String name;

    public TileLayerDTO(String name) {
        this(name, 1);
    }

    public TileLayerDTO(String name, int groundMovementCost) {
        this(name, groundMovementCost, TileType.GROUND);
    }

    public TileLayerDTO(String name, int groundMovementCost, TileType type) {
        this.name = name;
        this.groundMovementCost = groundMovementCost;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int getGroundMovementCost() {
        return this.groundMovementCost;
    }

    public TileType getType() {
        return this.type;
    }

}
