package marten.aoe.dto;

import java.io.Serializable;

public class TileLayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int groundMovementCost;
    private String name;

    public TileLayerDTO(String name, int groundMovementCost) {
        this.name = name;
        this.groundMovementCost = groundMovementCost;
    }

    public String getName() {
        return this.name;
    }

    public int getGroundMovementCost() {
        return this.groundMovementCost;
    }

}
