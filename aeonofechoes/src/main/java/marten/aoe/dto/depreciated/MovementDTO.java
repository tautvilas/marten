package marten.aoe.dto.depreciated;

import java.io.Serializable;

import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;

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
