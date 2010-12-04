package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.DefenseDTO;
import marten.aoe.proposal.dto.MovementDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.Tile;

public final class SnowForest extends Tile {
    public SnowForest(Map owner, Point coordinates) {
        super("Snowy forest", owner, coordinates);
    }
    @Override public DefenseDTO getDefenseBonus() {
        return new DefenseDTO(new int[][] {{+2, +1, 0}, {0, 0, 0}, {0, 0, 0}});
    }
    @Override public int getHeight() {
        return 0;
    }
    @Override public MovementDTO getMovementCost() {
        return new MovementDTO(new int[][] {{2, 3, 4}, {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, {1, 1, 1}});
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Difficult ground", "Obstacles", "Cover"};
    }
    @Override public void onTurnOver() {
    }
    @Override public void onUnitEntry() {
    }
    @Override public void onUnitExit() {
    }
}
