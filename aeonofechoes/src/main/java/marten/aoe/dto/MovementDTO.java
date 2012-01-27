package marten.aoe.dto;

import java.io.Serializable;

public final class MovementDTO implements Serializable {
    private static final long serialVersionUID = 7333580818044229109L;
    private final int movementCost;;
    public MovementDTO (int movementCost) {
        this.movementCost = movementCost;
    }
    public int getValue(UnitSize size, UnitType type) {
        return this.movementCost;
    }
}
