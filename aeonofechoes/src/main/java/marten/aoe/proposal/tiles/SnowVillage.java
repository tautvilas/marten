package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.DefenseDTO;
import marten.aoe.proposal.dto.MovementDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.Tile;

public final class SnowVillage extends Tile {
    public SnowVillage(Map owner, Point coordinates) {
        super("Snowy village", owner, coordinates);
    }
    @Override public DefenseDTO getDefenseBonus() {
        return new DefenseDTO(new int[][] {{+4, +2, 0}, {0, 0, 0}, {0, 0, 0}});
    }
    @Override public int getHeight() {
        return 0;
    }
    @Override public MovementDTO getMovementCost() {
        return new MovementDTO(new int[][] {{2, 2, 2}, {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, {1, 1, 1}});
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Difficult ground", "Hard cover"};
    }
    @Override public void onTurnOver() {
    }
    @Override public void onUnitEntry() {
    }
    @Override public void onUnitExit() {
    }
}
