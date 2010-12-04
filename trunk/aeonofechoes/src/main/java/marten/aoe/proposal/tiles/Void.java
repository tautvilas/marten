package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.DefenseDTO;
import marten.aoe.proposal.dto.MovementDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.Tile;

public final class Void extends Tile {
    public Void(Map owner, Point coordinates) {
        super("Void", owner, coordinates);
    }
    @Override public DefenseDTO getDefenseBonus() {
        return new DefenseDTO(new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
    }
    @Override public int getHeight() {
        return 0;
    }
    @Override public MovementDTO getMovementCost() {
        return new MovementDTO(new int[][] {{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}});
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Impassable"};
    }
    @Override public void onTurnOver() {
    }
    @Override public void onUnitEntry() {
        throw new RuntimeException("This tile type is inaccessible");
    }
    @Override public void onUnitExit() {
        throw new RuntimeException("This tile type is inaccessible");
    }
}
